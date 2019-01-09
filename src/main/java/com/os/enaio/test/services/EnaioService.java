package com.os.enaio.test.services;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.os.ecm.beans.EcmBeansException;
import com.os.ecm.beans.commons.EcmOption;
import com.os.ecm.beans.commons.FieldSchema;
import com.os.ecm.beans.commons.KeyType;
import com.os.ecm.beans.commons.SystemFieldType;
import com.os.ecm.beans.commons.helper.FieldSchemaItem;
import com.os.ecm.beans.commons.helper.QueryConfiguration;
import com.os.ecm.beans.generics.ecmentry.EcmDocument;
import com.os.ecm.beans.generics.ecmentry.EcmFolder;
import com.os.ecm.beans.generics.ecmentry.EcmObject;
import com.os.ecm.beans.generics.ecmentry.EcmRegister;
import com.os.ecm.beans.generics.template.GenericTemplate;
import com.os.ecm.beans.generics.template.dms.DmsQueryGenericTemplate;
import com.os.ecm.beans.generics.template.dms.ObjectDetailsGenericTemplate;
import com.os.ecm.jobs.template.custom.lic.CheckLicenseTemplate;
import com.os.enaio.test.services.templates.custom.GetGroupMembersTemplateExtended;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.*;

@Service
public class EnaioService {
    private final static Logger LOGGER = LoggerFactory.getLogger(EnaioService.class);

    @Autowired
    private GetGroupMembersTemplateExtended getGroupMembersTemplate;

    @Autowired
    private GenericTemplate genericTemplate;

    /// Using the license template explicit by name
    @Autowired
    @Qualifier(value = "LicTemplate")
    private CheckLicenseTemplate checkLicenseTemplate;

    public <T extends EcmObject> T getObjectDetails(String osId, SystemFieldType... sysFields) throws Exception {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("calling get of OSID " + osId);
        }

        ObjectDetailsGenericTemplate template = genericTemplate.getObjectDetailsGenericTemplate();
        return (T) template.get(osId, sysFields);
    }

    public String checkLicense(String module) throws Exception {
        String[] elements = checkLicenses(module);
        return elements.length == 0 ? "" : elements[0];
    }

    public String[] checkLicenses(String... modules) throws Exception {
        return Arrays.stream(Iterables.toArray(checkLicenseTemplate.checkLicense(modules), String.class))
                /// We filter since there're empty entries at the end, which we don't want
                .filter(s -> !Strings.isNullOrEmpty(s))
                .toArray(String[]::new);
    }

    public List<EcmObject> search(EcmObject searchObject, List<?> resultFieldList) throws EcmBeansException {
        DmsQueryGenericTemplate template = genericTemplate.getDmsQueryGenericTemplate();
        QueryConfiguration queryConfiguration = new QueryConfiguration();

        queryConfiguration.setFieldSchema(FieldSchema.MIN);
        List<FieldSchemaItem> queriedFields = new ArrayList<>();

        resultFieldList.forEach(field -> {
            if (field instanceof SystemFieldType) {
                queriedFields.add(new FieldSchemaItem((SystemFieldType) field));
            } else {
                queriedFields.add(new FieldSchemaItem(KeyType.INTERNAL_NAME, String.valueOf(field)));
            }
        });

        queryConfiguration.setQueriedFields(queriedFields, searchObject.getObjectTypeId(), searchObject.getObjectTypeId());

        return template.search(searchObject, queryConfiguration);
    }

    public List<EcmObject> search(EcmObject searchObject) throws EcmBeansException {
        return genericTemplate.getDmsQueryGenericTemplate().search(searchObject);
    }

    public <T extends EcmObject> T searchSingle(EcmObject searchObject) throws EcmBeansException {
        List<EcmObject> regs = search(searchObject);
        if (regs.size() > 0) {
            return (T) regs.get(0);
        }

        return null;
    }

    public void moveRegister(EcmRegister register, String targetObjectId) throws Exception {
        EcmObject target = getObjectDetails(targetObjectId);
        moveRegister(register, target);
    }

    public void moveRegister(EcmRegister register, EcmObject targetObject) throws EcmBeansException {
        Map<EcmOption, Boolean> optionMap = new HashMap<>();
        optionMap.put(EcmOption.COPYCASCADING, true);

        genericTemplate.move(targetObject, register, optionMap);
    }

    public String insert(EcmDocument document, EcmObject parent, File file) throws Exception {
        return genericTemplate.insertDocument(parent, document, file);
    }

    public String insert(EcmDocument document, String parentId, File file) throws Exception {
        EcmObject parent = getObjectDetails(parentId);
        return insert(document, parent, file);
    }

    public String insert(EcmRegister register, String parentId) throws Exception {
        EcmObject parent = getObjectDetails(parentId);
        return insert(register, parent);
    }

    public String insert(EcmRegister register, EcmObject parent) throws Exception {
        Map<EcmOption, Boolean> optionMap = new HashMap<>();
        optionMap.put(EcmOption.CHECKCATALOGUE, false);
        optionMap.put(EcmOption.CHECKKEYFIELDS, false);
        optionMap.put(EcmOption.CHECKOBLIGATION, false);
        optionMap.put(EcmOption.INITFIELDS, true);

        return genericTemplate.getXmlInsertGenericTemplate().insert(parent, register, optionMap).getOsid();
    }

    public String insert(EcmFolder folder) throws Exception {
        return genericTemplate.insertFolder(folder);
    }

    public NodeList getGroupMembers(String groupName) throws Exception {
        Document document = getGroupMembersTemplate.getGroupMembers(groupName);
        XPath xPath = XPathFactory.newInstance().newXPath();

        return (NodeList) xPath.evaluate("//Users/User", document, XPathConstants.NODESET);
    }

    public void update(EcmObject ecmObject) throws EcmBeansException {
        genericTemplate.update(ecmObject);
    }

    public void update(EcmDocument ecmObject, File... files) throws EcmBeansException {
        genericTemplate.update(ecmObject, files);
    }
}
