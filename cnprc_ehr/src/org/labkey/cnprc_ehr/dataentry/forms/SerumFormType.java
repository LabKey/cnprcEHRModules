package org.labkey.cnprc_ehr.dataentry.forms;

        import org.labkey.api.ehr.dataentry.DataEntryFormContext;
        import org.labkey.api.ehr.dataentry.TaskForm;
        import org.labkey.api.ehr.dataentry.TaskFormSection;
        import org.labkey.api.module.Module;
        import org.labkey.cnprc_ehr.dataentry.AnimalDetailsFormSection;
        import org.labkey.cnprc_ehr.dataentry.SerumFormSection;
        import org.labkey.api.view.template.ClientDependency;
        import org.labkey.api.ehr.dataentry.FormSection;

        import java.util.Arrays;

        public class SerumFormType extends TaskForm
        {

        public static final String NAME = "Serum Bank Update";
        public static final String LABEL = "Serum Bank Update";

        public SerumFormType(DataEntryFormContext ctx, Module owner)
        {
        super(ctx, owner, NAME, LABEL, "Colony Management", Arrays.asList(

        new AnimalDetailsFormSection(),
        new SerumFormSection(),
        new TaskFormSection()
        ));
                for (FormSection s : this.getFormSections())
                {
                        s.addConfigSource("Serum");
                }
                addClientDependency(ClientDependency.fromPath("cnprc_ehr/model/sources/Serum.js"));

        }

        }
