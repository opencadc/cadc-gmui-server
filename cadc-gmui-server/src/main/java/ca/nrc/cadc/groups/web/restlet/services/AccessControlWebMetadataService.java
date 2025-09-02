package ca.nrc.cadc.groups.web.restlet.services;

import org.restlet.service.MetadataService;
import org.restlet.data.MediaType;


/**
 * Metadata service to add more extensions.
 */
public class AccessControlWebMetadataService extends MetadataService
{
    public AccessControlWebMetadataService()
    {
        super();
        addExtension("img", MediaType.IMAGE_ALL, true);
        addExtension("all", MediaType.ALL);
    }
}