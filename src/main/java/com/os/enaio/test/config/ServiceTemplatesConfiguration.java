package com.os.enaio.test.config;

import com.os.ecm.beans.generics.template.GenericTemplate;
import com.os.ecm.jobs.CallerSource;
import com.os.ecm.jobs.template.custom.lic.CheckLicenseTemplate;
import com.os.ecm.jobs.template.dms.GetObjectDetailsTemplate;
import com.os.ecm.jobs.template.mng.GetUserAttributesTemplate;
import com.os.enaio.test.services.templates.custom.GetGroupMembersTemplateExtended;
import com.os.services.blue.connector.ConnectionConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by cschulze on 15.03.2017.
 */
@Configuration
@Import({ConnectionConfiguration.class})
public class ServiceTemplatesConfiguration {

    /// A Spring Component can be used as an class property (this one is NOT currently used) ...
    @Autowired
    @Qualifier("public.technicalCallerSource")
    private CallerSource technicalCallerSource;

    /// Or inline as a method parameter.
    /// Also Beans can be addressed implicit by their type (but only as long as its identification is unique) or explicit by name.
    /// By name is also required to use, when there're more than one Bean returning that Type.
    @Bean(name = "enaio.template.getUserAttributesTemplate")
    public GetUserAttributesTemplate getUserAttributesTemplate(@Qualifier("public.technicalCallerSource") CallerSource techCallerSource) {
        return new GetUserAttributesTemplate(techCallerSource);
    }

    @Bean(name = "enaio.template.getObjectDetailsTemplate")
    public GetObjectDetailsTemplate getObjectDetailsTemplate(@Qualifier("public.technicalCallerSource") CallerSource techCallerSource) {
        return new GetObjectDetailsTemplate(techCallerSource);
    }

    @Bean
    public GenericTemplate getGenericTemplate(@Qualifier("public.technicalCallerSource") CallerSource techCallerSource) {
        GenericTemplate genericTemplate = new GenericTemplate();
        genericTemplate.setCallerSource(techCallerSource);

        return genericTemplate;
    }

    @Bean
    public GetGroupMembersTemplateExtended getGroupMembersTemplate(@Qualifier("public.technicalCallerSource") CallerSource techCallerSource) {
        GetGroupMembersTemplateExtended template = new GetGroupMembersTemplateExtended();
        template.setCallerSource(techCallerSource);

        return template;
    }

    /// Here we want to reference this template by name
    @Bean(name = "LicTemplate")
    public CheckLicenseTemplate getCheckLicenseTemplate(@Qualifier("public.technicalCallerSource") CallerSource techCallerSource) {
        CheckLicenseTemplate checkLicenseTemplate = new CheckLicenseTemplate();
        checkLicenseTemplate.setCallerSource(techCallerSource);
        return checkLicenseTemplate;
    }
}
