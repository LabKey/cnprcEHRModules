package org.labkey.cnprc_ehr.query;

import org.jetbrains.annotations.NotNull;
import org.labkey.api.data.DbSchema;
import org.labkey.api.data.DbSchemaType;
import org.labkey.api.data.JdbcType;
import org.labkey.api.data.SQLFragment;
import org.labkey.api.data.Sort;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.VirtualTable;
import org.labkey.api.query.ExprColumn;
import org.labkey.api.query.UserSchema;
import org.labkey.api.study.Dataset;
import org.labkey.api.study.Study;
import org.labkey.api.study.StudyService;
import org.labkey.cnprc_ehr.CNPRC_EHRSchema;

public class ReproductiveCalendarTable extends VirtualTable
{
	public ReproductiveCalendarTable(UserSchema userSchema)
	{
		super(DbSchema.get("studydataset", DbSchemaType.Provisioned), CNPRC_EHRSchema.REPRODUCTIVE_CALENDAR, userSchema);
		setupColumns();
	}

	@Override @NotNull
	public SQLFragment getFromSQL()
	{
		String datasetSchema = "studydataset.";
		String cycle = datasetSchema + getDatasetTableName("cycle");
		String breeding = datasetSchema + getDatasetTableName("breeding");
		String pregnancyConfirmation = datasetSchema + getDatasetTableName("pregnancyConfirmation");
		String demographics = datasetSchema + getDatasetTableName("demographics");
		String arrival = datasetSchema + getDatasetTableName("arrival");
		String birth = datasetSchema + getDatasetTableName("birth");
		String deaths = datasetSchema + getDatasetTableName("deaths");
		String departure = datasetSchema + getDatasetTableName("departure");
		String pregnancyDeterminations = datasetSchema + getDatasetTableName("pregnancyDeterminations");
		String menses = datasetSchema + getDatasetTableName("menses");
		String morningHealthObs = datasetSchema + getDatasetTableName("morningHealthObs");
		String matings = datasetSchema + getDatasetTableName("matings");

		SQLFragment sql = new SQLFragment();
		sql.append
				(
						"   SELECT \n" +
								"       animal02, \n" +
								"       an_sex02, \n" +
								"       rep_year02, \n" +
								"       rep_month02, \n" +
								"       rep_month_alpha02, \n" +
								"       [01], \n" +
								"       [02], \n" +
								"       [03], \n" +
								"       [04], \n" +
								"       [05], \n" +
								"       [06], \n" +
								"       [07], \n" +
								"       [08], \n" +
								"       [09], \n" +
								"       [10], \n" +
								"       [11], \n" +
								"       [12], \n" +
								"       [13], \n" +
								"       [14], \n" +
								"       [15], \n" +
								"       [16], \n" +
								"       [17], \n" +
								"       [18], \n" +
								"       [19], \n" +
								"       [20], \n" +
								"       [21], \n" +
								"       [22], \n" +
								"       [23], \n" +
								"       [24], \n" +
								"       [25], \n" +
								"       [26], \n" +
								"       [27], \n" +
								"       [28], \n" +
								"       [29], \n" +
								"       [30], \n" +
								"       [31] \n" +
								"   FROM \n" +
								"   ( \n" +
								"       select  x00.animal01 as animal02, \n" +
								"               x00.an_sex01 as an_sex02, \n" +
								"               x00.rep_year01 as rep_year02, \n" +
								"               x00.rep_month01 as rep_month02, \n" +
								"               x00.rep_month_alpha01 as rep_month_alpha02, \n" +
								"               x00.rep_day01 as rep_day02, \n" +
								"               ( \n" +
								"                   coalesce(x00.mensesObs,'') + \n" +
								"                   coalesce(x00.pregDetObs,'') + \n" +
								"                   coalesce(x00.breedingObs,'') + \n" +
								"                   coalesce(x00.matingObs,'') + \n" +
								"                   coalesce(x00.cycleObs,'') + \n" +
								"                   coalesce(x00.pregConObs01,'') + \n" +
								"                   coalesce(x00.pregConObs02,'') + \n" +
								"                   coalesce(x00.pregConObs03,'') + \n" +
								"                   coalesce(x00.pregConObs04,'') + \n" +
								"                   coalesce(x00.pregConObs05,'') + \n" +
								"                   coalesce(x00.pregConObs06,'') + \n" +
								"                   coalesce(x00.pregConObs07,'') + \n" +
								"                   coalesce(x00.pregConObs08,'') + \n" +
								"                   coalesce(x00.pregConObs09,'') + \n" +
								"                   coalesce(x00.pregConObs10,'') \n" +
								"               )   as this_symbol02 \n" +
								"       FROM \n" +
								"       ( \n" +
								"           select  x01.animal as animal01, \n" +
								"                   x01.an_sex as an_sex01, \n" +
								"                   datepart(yyyy, x01.this_date) as rep_year01, \n" +
								"                   datepart(mm, x01.this_date) as rep_month01, \n" +
								"                   datename(month, x01.this_date) as rep_month_alpha01, \n" +
								"                   datepart(dd,x01.this_date) as rep_day01, \n" +
								"                   x01.begin_this_month as begin_this_month01, \n" +
								"                   x01.this_date as this_date01, \n" +
								"                   coalesce \n" +
								"                   ( \n" +
								"                   	( \n" +
								"                       	case	mh01.observation \n" +
								"                       	when	'NRMLMEN' \n" +
								"                       	then	case	ac1.date \n" +
								"                               	when	x01.this_date \n" +
								"                               	then \n" +
								"                                   	case \n" +
								"                                   	when \n" +
								"                                   	(	datediff \n" +
								"                                       	(	d, \n" +
								"                                           	(	select  max(ac2.date) \n" +
								"                                               	from  " + cycle + " ac2  \n" +
								"                                               	where 	ac2.participantid = x01.animal  \n" +
								"                                               	and     ac2.date < ac1.date  \n" +
								"                                           	),  \n" +
								"                                           	ac1.date  \n" +
								"                                       	)  \n" +
								"								    	) < 40  \n" +
								"                                   	THEN  \n" +
								"                                   	(	cast  \n" +
								"                                       	(  \n" +
								"                                           	datediff  \n" +
								"                                           	(	d,  \n" +
								"                                               	(	select 	max(ac2.date)  \n" +
								"                                                   	from  " + cycle + " ac2 \n" +
								"                                                   	where 	ac2.participantid = x01.animal \n" +
								"                                                   	and 	ac2.date < ac1.date \n" +
								"                                               	), \n" +
								"                                               	ac1.date  \n" +
								"                                           	)  \n" +
								"                                           	as varchar  \n" +
								"                                       	) \n" +
								"								    	) + 'M' \n" +
								"							        	else 'M'  \n" +
								"                                   	end  \n" +
								"					       			else 'm' \n" +
								"                  					end \n" +
								"                       	when	'HEVYMEN' \n" +
								"                       	then	case    ac1.date  \n" +
								"                               	when    x01.this_date then 'H'  \n" +
								"						        	else 'h'  \n" +
								"                               	end \n" +
								"                       	end \n" +
								"						), \n" +
								"                   	( \n" +
								"                       	case	ms01.observationCode \n" +
								"                       	when	'M' \n" +
								"                       	then	case	ac1.date \n" +
								"                               	when	x01.this_date \n" +
								"                               	then \n" +
								"                                   	case \n" +
								"                                   	when \n" +
								"                                   	(	datediff \n" +
								"                                       	(	d, \n" +
								"                                           	(	select  max(ac2.date) \n" +
								"                                               	from  " + cycle + " ac2  \n" +
								"                                               	where 	ac2.participantid = x01.animal  \n" +
								"                                               	and     ac2.date < ac1.date  \n" +
								"                                           	),  \n" +
								"                                           	ac1.date  \n" +
								"                                       	)  \n" +
								"								    	) < 40  \n" +
								"                                   	THEN  \n" +
								"                                   	(	cast  \n" +
								"                                       	(  \n" +
								"                                           	datediff  \n" +
								"                                           	(	d,  \n" +
								"                                               	(	select 	max(ac2.date)  \n" +
								"                                                   	from  " + cycle + " ac2 \n" +
								"                                                   	where 	ac2.participantid = x01.animal \n" +
								"                                                   	and 	ac2.date < ac1.date \n" +
								"                                               	), \n" +
								"                                               	ac1.date  \n" +
								"                                           	)  \n" +
								"                                           	as varchar  \n" +
								"                                       	) \n" +
								"								    	) + 'M' \n" +
								"							        	else 'M'  \n" +
								"                                   	end  \n" +
								"					       			else 'm' \n" +
								"                  					end \n" +
								"                       	when	'H' \n" +
								"                       	then	case    ac1.date  \n" +
								"                               	when    x01.this_date then 'H'  \n" +
								"						        	else 'h'  \n" +
								"                               	end \n" +
								"                       	end \n" +
								"						) \n" +
								"		            ) as mensesObs, \n" +
								"                   ( \n" +
								"                       case    pd01.method \n" +
								"                       when    'B'  \n" +
								"                       then  \n" +
								"                           case	pd01.result \n" +
								"                           when    '+' 	then	'B' \n" +
								"                           when    '-'	    then    'b' \n" +
								"                           end \n" +
								"                       when    'P' \n" +
								"                       then  \n" +
								"                           case	pd01.result \n" +
								"                           when    '+' 	then	'P' \n" +
								"                           when    '-'	    then    'p' \n" +
								"                           end \n" +
								"                       when    'US' \n" +
								"                       then \n" +
								"                           case	pd01.result \n" +
								"                           when    '+' 	then	'U' \n" +
								"                           when    '-'	    then    'u' \n" +
								"                           when    'T' 	then	'T' \n" +
								"                           when    'e'	    then    'E' \n" +
								"                           end \n" +
								"                       end \n" +
								"		            ) as pregDetObs	, \n" +
								"                   ( \n" +
								"                       case	br01.obsCode \n" +
								"                       when	'X' 	then    'X' \n" +
								"                       when    '+'	    then '+' \n" +
								"                       when    '-'	    then '-' \n" +
								"                       when    'A'	    then 'A' \n" +
								"                       when	'F'	    then 'F' \n" +
								"                       end  \n" +
								"		            ) as breedingObs, \n" +
								"                   ( \n" +
								"                       case \n" +
								"                       when    (am01.date >= sysdatetime()) THEN 's'  \n" +
								"                       when    (	(am01.date < sysdatetime()) \n" +
								"                                   and \n" +
								"                                   (	not exists \n" +
								"                                       (	select 	'x' \n" +
								"                                           from 	" + breeding + " br02  \n" +
								"                                           where 	br02.participantid = x01.animal \n" +
								"                                           and     br02.date = x01.this_date \n" +
								"                                           and 	(	(br02.obsCode='X') \n" +
								"                                                       or \n" +
								"                                                       (br02.obsCode='+') \n" +
								"                                                       or \n" +
								"                                                       (br02.obsCode='-') \n" +
								"                                                       or \n" +
								"                                                       (br02.obsCode='F') \n" +
								"                                                   ) \n" +
								"							            ) \n" +
								"						            ) \n" +
								"					            ) THEN 's' \n" +
								"                       end \n" +
								"		            ) as matingObs, \n" +
								"                   ( \n" +
								"                       (	select 	'*' \n" +
								"                           from    " + cycle + " \n" +
								"                           where 	participantid = x01.animal \n" +
								"                           and     ( \n" +
								"                                       case \n" +
								"                                       when methodOne is not null  \n" +
								"                                       then \n" +
								"                                           case  \n" +
								"                                           when ((gestDayOne is not null) and (gestDayOne != 0))  \n" +
								"                                           then \n" +
								"                                               case \n" +
								"                                               when schedStatusOne is null \n" +
								"                                               then dateadd(day, gestDayOne, estGestStartDate) \n" +
								"									            else null \n" +
								"                                               end \n" +
								"								            else null \n" +
								"                                           end \n" +
								"							            else null \n" +
								"                                       end \n" +
								"						            ) = x01.this_date \n" +
								"                           and 	( \n" +
								"                                       case \n" +
								"                                       when estGestStartDate is not null \n" +
								"                                       then \n" +
								"                                           case \n" +
								"                                           when methodOne is not null \n" +
								"                                           then \n" +
								"                                               case \n" +
								"                                               when ( (gestDayOne is not null) and (gestDayOne != 0) ) \n" +
								"                                               then \n" +
								"                                                   case \n" +
								"                                                   when \n" +
								"                                                   (	case \n" +
								"                                                       when methodOne is not null \n" +
								"                                                       then \n" +
								"                                                           case \n" +
								"                                                           when ((gestDayOne is not null) and (gestDayOne != 0)) \n" +
								"                                                           then \n" +
								"                                                               case \n" +
								"                                                               when schedStatusOne is null \n" +
								"                                                               then dateadd(day, gestDayOne, estGestStartDate) \n" +
								"												                else null \n" +
								"                                                               end \n" +
								"											                else null \n" +
								"                                                           end \n" +
								"										                else null \n" +
								"                                                       end \n" +
								"									                ) is not null \n" +
								"                                                   then '*' \n" +
								"									                else null \n" +
								"                                                   end \n" +
								"									            else null \n" +
								"                                               end \n" +
								"								            else null \n" +
								"                                           end \n" +
								"							            else null \n" +
								"                                       end \n" +
								"						            ) = '*' \n" +
								"			            ) \n" +
								"		            ) as cycleObs, \n" +
								"                   ( \n" +
								"                       (	select 	'C' \n" +
								"                           where 	exists \n" +
								"                                   (	select 	'x' \n" +
								"                                       from    " + pregnancyConfirmation + " \n" +
								"                                       where 	conception = x01.this_date \n" +
								"                                       and    participantid = x01.animal \n" +
								"						            ) \n" +
								"			            ) \n" +
								"		            ) as pregConObs01, \n" +
								"                   ( \n" +
								"                       (	select 	'LV' \n" +
								"                           where 	exists \n" +
								"                                   (	select 	'x' \n" +
								"                                       from    " + pregnancyConfirmation +  " \n" +
								"                                       where 	termDate = x01.this_date \n" +
								"                                       and     participantid = x01.animal \n" +
								"                                       and 	birthViability='L' \n" +
								"                                       and 	deliveryMode='V' \n" +
								"						            ) \n" +
								"			            ) \n" +
								"		            ) as	pregConObs02, \n" +
								"                   ( \n" +
								"                       (	select 	'LVX' \n" +
								"                           where 	exists \n" +
								"                                   (	select 	'x' \n" +
								"                                       from 	" + pregnancyConfirmation +  " \n" +
								"                                       where 	termDate = x01.this_date \n" +
								"                                       and     participantid = x01.animal \n" +
								"                                       and 	birthViability = 'L' \n" +
								"                                       and     deliveryMode = 'VX' \n" +
								"						            ) \n" +
								"			            ) \n" +
								"		            ) as pregConObs03,  \n" +
								"                   (  \n" +
								"                       (	select 	'LN' \n" +
								"                           where 	exists \n" +
								"                                   (	select 	birthViability + deliveryMode \n" +
								"                                       from 	" + pregnancyConfirmation +  " \n" +
								"                                       where 	termDate = x01.this_date \n" +
								"                                       and 	participantid = x01.animal \n" +
								"                                       and 	birthViability='L' \n" +
								"                                       and 	deliveryMode='N' \n" +
								"                                   ) \n" +
								"                       ) \n" +
								"                   ) as pregConObs04, \n" +
								"                   ( \n" +
								"                       (	select 	'LNX' \n" +
								"                           where 	exists \n" +
								"                                   (	select 	birthViability + deliveryMode \n" +
								"                                       from 	" + pregnancyConfirmation +  " \n" +
								"                                       where 	termDate = x01.this_date \n" +
								"                                       and 	participantid = x01.animal \n" +
								"                                       and 	birthViability='L' \n" +
								"                                       and 	deliveryMode='NX' \n" +
								"                                   ) \n" +
								"                       ) \n" +
								"                   ) as pregConObs05, \n" +
								"                   ( \n" +
								"                       (	select 	'DV' \n" +
								"                           where 	exists \n" +
								"                                   (	select 	birthViability + deliveryMode \n" +
								"										from 	" + pregnancyConfirmation +  " \n" +
								"										where 	termDate = x01.this_date \n" +
								"										and 	participantid = x01.animal \n" +
								"										and 	birthViability='D' \n" +
								"										and 	deliveryMode='V' \n" +
								"                					) \n" +
								"    					) \n" +
								"					) as pregConObs06, \n" +
								"					( \n" +
								"						(	select 	'DVX' \n" +
								"							where 	exists \n" +
								"									(	select 	birthViability + deliveryMode \n" +
								"										from 	" + pregnancyConfirmation +  " \n" +
								"										where 	termDate = x01.this_date \n" +
								"										and 	participantid = x01.animal  \n" +
								"										and 	birthViability='D' \n" +
								"										and 	deliveryMode='VX' \n" +
								"                					) \n" +
								"    					) \n" +
								"					) as pregConObs07,  \n" +
								"					( \n" +
								"						(	select 	'DN' \n" +
								"							where 	exists \n" +
								"									(	select 	birthViability + deliveryMode \n" +
								"										from 	" + pregnancyConfirmation +  " \n" +
								"										where 	termDate = x01.this_date \n" +
								"										and 	participantid = x01.animal \n" +
								"										and 	birthViability='D' \n" +
								"										and 	deliveryMode='N' \n" +
								"                					) \n" +
								"    					) \n" +
								"					) as pregConObs08, \n" +
								"					( \n" +
								"						(	select 	'DNX' \n" +
								"							where 	exists \n" +
								"									(	select 	birthViability + deliveryMode \n" +
								"										from	" + pregnancyConfirmation +  " \n" +
								"										where 	termDate = x01.this_date \n" +
								"										and 	participantid = x01.animal \n" +
								"										and 	birthViability = 'D' \n" +
								"										and 	deliveryMode='NX' \n" +
								"                					) \n" +
								"    					) \n" +
								"					) as pregConObs09, \n" +
								"					( \n" +
								"						( \n" +
								"        					select 	'D' \n" +
								"							where 	exists \n" +
								"									( \n" +
								"										select 	birthViability + deliveryMode \n" +
								"										from 	" + pregnancyConfirmation +  " \n" +
								"										where 	termDate = x01.this_date \n" +
								"										and 	participantid = x01.animal \n" +
								"										and 	birthViability = 'D' \n" +
								"										and 	(	deliveryMode not in ('V','N','VX','NX') \n" +
								"													or \n" +
								"													deliveryMode is null \n" +
								"                            					) \n" +
								"                					) \n" +
								"    					) \n" +
								"					) as pregConObs10 \n" +
								"			from \n" +
								"			( \n" +
								"        		select \n" +
								"        			cal.animal as animal, \n" +
								"        			cal.animal_acquisition_month as animal_acquisition_month, \n" +
								"        			cal.months_back  as months_back, \n" +
								"        			cal.begin_this_month as begin_this_month,  \n" +
								"        			cal.end_this_month as end_this_month, \n" +
								"        			dateadd(day,-1,(dateadd(day, dys.day_no, begin_this_month))) as this_date, \n" +
								"        			cal.an_sex	 as an_sex  \n" +
								"        		from \n" +
								"              	( \n" +
								"                	select 1 as day_no union \n" +
								"                 	select 2 union \n" +
								"               	select 3 union \n" +
								"                	select 4 union \n" +
								"                	select 5 union \n" +
								"             		select 6 union \n" +
								"                 	select 7 union \n" +
								"                	select 8 union \n" +
								"            		select 9 union \n" +
								"               	select 10 union \n" +
								"               	select 11 union \n" +
								"            		select 12 union \n" +
								"              		select 13 union \n" +
								"           		select 14 union \n" +
								"                	select 15 union \n" +
								"           		select 16 union \n" +
								"              		select 17 union \n" +
								"               	select 18 union \n" +
								"             		select 19 union \n" +
								"               	select 20 union \n" +
								"                  	select 21 union \n" +
								"               	select 22 union \n" +
								"                 	select 23 union \n" +
								"                  	select 24 union \n" +
								"                	select 25 union \n" +
								"                	select 26 union \n" +
								"                 	select 27 union \n" +
								"              		select 28 union \n" +
								"                	select 29 union \n" +
								"                  	select 30	union \n" +
								"                  	select 31 \n" +
								"              	)	dys, \n" +
								"        		( \n" +
								"                	select \n" +
								"        				ai.animal as animal, \n" +
								"        				ai.animal_acquisition_month as animal_acquisition_month, \n" +
								"        				mb.months_back as months_back, \n" +
								"        				dateadd(month,mb.months_back,ai.begin_of_last_month) as begin_this_month, \n" +
								"        				EOMONTH(dateadd(month, mb.months_back, begin_of_last_month)) as end_this_month, \n" +
								"        				ai.an_sex	as an_sex \n" +
								"        			from \n" +
								"                	( \n" +
								"                     	 select -23 as months_back union \n" +
								"                        select -22 union \n" +
								"                        select -21 union \n" +
								"                        select -20 union \n" +
								"                        select -19 union \n" +
								"                        select -18 union \n" +
								"                        select -17 union \n" +
								"                        select -16 union \n" +
								"                        select -15 union \n" +
								"                        select -14 union \n" +
								"                        select -13 union \n" +
								"                        select -12 union \n" +
								"                        select -11 union \n" +
								"                        select -10 union \n" +
								"                        select -9 union \n" +
								"                        select -8 union \n" +
								"                        select -7 union \n" +
								"                        select -6 union \n" +
								"                        select -5 union \n" +
								"                        select -4 union \n" +
								"                        select -3 union \n" +
								"                        select -2 union \n" +
								"                        select -1 union \n" +
								"                        select 0 union \n" +
								"                        select 1 \n" +
								"                	)	mb, \n" +
								"        			( \n" +
								"                		select  \n" +
								"        					dem01.participantid as animal, \n" +
								"        					coalesce \n" +
								"                			( \n" +
								"                        		dateadd(month, datediff(month, 0, br01.date),0), \n" +
								"                        		dateadd(month, datediff(month, 0, ar01.date),0) \n" +
								"                			) as animal_acquisition_month, \n" +
								"							case	dem01.calculated_status \n" +
								"							when 	'Alive' then dateadd(month, datediff(month, 0, sysdatetime()),0) \n" +
								"                    		else 	coalesce \n" +
								"									( \n" +
								"    									dateadd(month, datediff(month, 0, dth01.date),0), \n" +
								"    									dateadd(month, datediff(month, 0, dep01.date),0) \n" +
								"									) \n" +
								"							end as begin_of_last_month, \n" +
								"							dem01.gender as an_sex \n" +
								"						from	" + demographics +  " dem01 \n" +
								"							LEFT OUTER JOIN " + arrival + " ar01  \n" +
								"							on dem01.participantid = ar01.participantid, \n" +
								"							" + demographics + " dem02 \n" +
								"							LEFT OUTER JOIN " + birth + " br01 \n" +
								"							on dem02.participantid = br01.participantid, \n" +
								"							" + demographics + " dem03 \n" +
								"							LEFT OUTER JOIN " + deaths + " dth01 \n" +
								"							on dem03.participantid = dth01.participantid, \n" +
								"							" + demographics + " dem04 \n" +
								"							LEFT OUTER JOIN " + departure + " dep01 \n" +
								"							on dem04.participantid = dep01.participantid \n" +
								"							where	dem01.participantid = dem02.participantid \n" +
								"							and		dem01.participantid = dem03.participantid \n" +
								"							and		dem01.participantid = dem04.participantid \n" +
								"    				) ai \n" +
								"				) cal \n" +
								"				where 	(dys.day_no <= (datediff(day, cal.begin_this_month, cal.end_this_month) + 1)) \n" +
								"				and 		(cal.begin_this_month >= cal.animal_acquisition_month) \n" +
								"			) x01 \n" +
								"			LEFT OUTER JOIN 	" + cycle +  " ac1 \n" +
								"			on 	ac1.participantid = x01.animal \n" +
								"			and	cast(ac1.date as Date) = cast(x01.this_date as Date) \n" +
								" 			\n" +
								"			LEFT OUTER JOIN 	" + pregnancyDeterminations + " pd01 \n" +
								"			on 	pd01.participantid = x01.animal \n" +
								"			and	cast(pd01.date as Date) = cast(x01.this_date as Date) \n" +
								"			 \n" +
								"			LEFT OUTER JOIN 	" + menses + " ms01 \n" +
								"			on 	ms01.participantid = x01.animal  \n" +
								"			and	cast(ms01.date as Date) = cast(x01.this_date as Date)  \n" +
								"			 \n" +
								"			LEFT OUTER JOIN 	" + morningHealthObs + " mh01 \n" +
								"			on 	mh01.participantid = x01.animal  \n" +
								"			and	cast(mh01.date as Date) = cast(x01.this_date as Date)  \n" +
								"			 \n" +
								"			LEFT OUTER JOIN 	" + breeding + " br01 \n" +
								"			on 	br01.participantid = x01.animal \n" +
								"			and	cast(br01.date as Date) = cast(x01.this_date as Date) \n" +
								"			 \n" +
								"			LEFT OUTER JOIN 	" + matings + " am01 \n" +
								"			on 	am01.participantid = x01.animal \n" +
								"			and	cast(am01.date as Date) = cast(x01.this_date as Date) \n" +
								"			 \n" +
								"			where		(x01.an_sex = 'F') \n" +
								"	) x00 \n" +
								") a \n" +
								"pivot (max(this_symbol02) for rep_day02 in ([01],[02],[03],[04],[05],[06],[07],[08],[09],[10],[11],[12],[13],[14],[15],[16],[17],[18],[19],[20],[21],[22],[23],[24],[25],[26],[27],[28],[29],[30],[31])) as symbol \n");

		return sql;
	}

	private void setupColumns()
	{

		addColumn(new ExprColumn(this, "id", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".animal02"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "sex", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".an_sex02"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "rcYear", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".rep_year02"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "rcMonth", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".rep_month02"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "rcMonthAlpha", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".rep_month_alpha02"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "01", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[01]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "02", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[02]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "03", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[03]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "04", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[04]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "05", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[05]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "06", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[06]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "07", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[07]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "08", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[08]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "09", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[09]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "10", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[10]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "11", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[11]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "12", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[12]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "13", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[13]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "14", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[14]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "15", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[15]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "16", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[16]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "17", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[17]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "18", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[18]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "19", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[19]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "20", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[20]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "21", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[21]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "22", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[22]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "23", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[23]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "24", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[24]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "25", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[25]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "26", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[26]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "27", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[27]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "28", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[28]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "29", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[29]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "30", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[30]"), JdbcType.VARCHAR));
		addColumn(new ExprColumn(this, "31", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".[31]"), JdbcType.VARCHAR));
	}

	@Override
	public boolean isPublic()
	{
		return true;
	}

	public String getDatasetTableName(String datasetName)
	{
		Study study = StudyService.get().getStudy(getUserSchema().getContainer());
		Dataset dataset = study.getDatasetByName(datasetName);
		TableInfo tableInfo = dataset.getTableInfo(getUserSchema().getUser());
		return tableInfo.getDomain().getStorageTableName();
	}
}