/*
 * Copyright (c) 2016 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.labkey.cnprc_ehr;

import org.labkey.api.action.SimpleRedirectAction;
import org.labkey.api.action.SpringActionController;
import org.labkey.api.data.Container;
import org.labkey.api.security.RequiresPermission;
import org.labkey.api.security.permissions.ReadPermission;
import org.labkey.api.util.PageFlowUtil;
import org.labkey.api.view.ActionURL;

public class CNPRC_EHRController extends SpringActionController
{
    private static final DefaultActionResolver _actionResolver = new DefaultActionResolver(CNPRC_EHRController.class);
    public static final String NAME = "cnprc_ehr";

    public CNPRC_EHRController()
    {
        setActionResolver(_actionResolver);
    }

    public static class ObservationCodeForm
    {
        private int _rowId;

        public int getRowId()
        {
            return _rowId;
        }

        public void setRowId(int rowId)
        {
            _rowId = rowId;
        }
    }

    @RequiresPermission(ReadPermission.class)
    public class ObservationCodeDetailAction extends SimpleRedirectAction<ObservationCodeForm>
    {
        @Override
        public ActionURL getRedirectURL(ObservationCodeForm form) throws Exception
        {
            Container c = getViewContext().getContainer();
            String encodedContainerPath = PageFlowUtil.encode(c.getPath());
            ActionURL url = new ActionURL("query" + encodedContainerPath + "%2FdetailsQueryRow.view");
            url.addParameter("schemaName", "cnprc_ehr");
            url.addParameter("query.queryName", "observation_types");
            url.addParameter("rowId", form.getRowId());
            return url;
        }
    }
}