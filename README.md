## Summary
This repository contains all the modules required for the UC Davis California National Primate Research Center (CNPRC) LabKey server, a system for managing and reporting on electronic health records (EHR) and related research data for the CNPRC primate colony. The server helps the center gain insight into combined lab, clinical and operational information; meet regulatory requirements; and coordinate the activities required to support animal care and research projects.

<a name="modules"></a>
## Modules
1. cnprc_ehr: The main EHR module for managing primate care
2. cnprc_genetics: Tracks primate genetic data
3. cnprc_pdl: Serology/Virology data from the pathology detection laboratory
4. cnprc_complianceAndTraining: Coordinates employee compliance training
5. cnprc_billing: Automates the billing process by collecting clinical, research and operational activities as billable items and generating invoices.  

<a name="setUp"></a>
## Developer Set Up
1. Clone this repository into the externalModules directory of your LabKey project.  
1. Edit your project settings.gradle file to build the externalModules/cnprcEHRModules directory.
1. Build the module with either gradle command 
    * gradlew deployapp 
    * gradlew :externalModules:cnprcEHRModules deployModule
