package io.spring.resource.sftp;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * ResourceLoader implementation to obtain the SftpResource
 * implementation responsible for handling SFTP resources.
 *
 * @author Chris Schaefer
 */
public class SftpResourceLoader implements ResourceLoader {
	private static final String SFTP_PREFIX = "sftp:";

	private final ResourceLoader delegate;
	private final ApplicationContext applicationContext;

	public SftpResourceLoader(ApplicationContext applicationContext, ResourceLoader delegate) {
		this.delegate = delegate;
		this.applicationContext = applicationContext;
	}

	@Override
	public Resource getResource(String resource) {
		if (resource.startsWith(SFTP_PREFIX)) {
			SftpResource sftpResource = applicationContext.getBean(SftpResource.class);

			return sftpResource.getResource(resource.substring(SFTP_PREFIX.length()));
		}

		return delegate.getResource(resource);
	}

	@Override
	public ClassLoader getClassLoader() {
		return delegate.getClassLoader();
	}
}
