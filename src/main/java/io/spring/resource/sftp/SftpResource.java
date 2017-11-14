package io.spring.resource.sftp;

import org.springframework.core.io.Resource;

/**
 * SftpResource interface definition.
 *
 * Resource identifier format: sftp:user:pass@some.host.com:6666/filename.csv
 *
 * For security reasons, if you prefer not to pass the username and password
 * in the resource identifier, you must set the following environment variables:
 *
 * sftp_username - the SFTP username to use
 * sftp_password - the SFTP password to use
 *
 * @author Chris Schaefer
 */
public interface SftpResource {
	Resource getResource(String resource);
}
