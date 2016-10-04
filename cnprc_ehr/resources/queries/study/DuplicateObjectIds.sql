SELECT StudyData.DataSet,StudyData.objectid
FROM StudyData 
group by StudyData.DataSet,StudyData.objectid having count(*) > 1