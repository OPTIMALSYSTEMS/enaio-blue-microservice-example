package com.os.enaio.test.services.templates.custom;

import com.os.ecm.jobs.template.custom.mng.GetGroupMembersTemplate;
import org.w3c.dom.Document;

import java.util.HashMap;

public class GetGroupMembersTemplateExtended extends GetGroupMembersTemplate {

    public Document getGroupMembers(String groupName) throws Exception {
        HashMap<String, Object> jobParams = new HashMap<>();
        jobParams.put("Flags", 0);
        jobParams.put("GroupName", groupName);
        return execute(jobParams);
    }
}
