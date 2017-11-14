package io.spring.resource.sftp;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.file.remote.InputStreamCallback;
import org.springframework.integration.file.remote.RemoteFileOperations;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.stereotype.Component;

/**
 * SftpResource implementation utilizing a Spring Integration SftpRemoteFileTemplate
 * to connect and return a file as a Spring Resource.
 *
 * @author Chris Schaefer
 */
@Component
public class SftpRemoteFileTemplateSftpResource implements SftpResource {
	private static final String SFTP_URI_PREFIX = "sftp://";

	@Value("${sftp_username}")
	private String username;

	@Value("${sftp_password}")
	private String password;

	@Override
	public Resource getResource(String resource) {
		URI sftpUri = getUri(resource);

		RemoteFileOperations remoteFileOperations = new SftpRemoteFileTemplate(getSessionFactory(sftpUri));

		FileFetcher filefetcher = new FileFetcher();

		remoteFileOperations.get(sftpUri.getPath(), filefetcher);

		return new ByteArrayResource(filefetcher.getBytes());
	}

	private URI getUri(String resource) {
		URI uri = null;

		try {
			uri = new URI(SFTP_URI_PREFIX + resource);

			if (uri.getUserInfo() == null) {
				uri = buildUriFromEnvironment(uri, resource);
			}
		}
		catch (Exception e) {
			throw new RuntimeException("Provided URI for SFTP resource is not valid: " + resource, e);
		}

		return uri;
	}

	private DefaultSftpSessionFactory getSessionFactory(URI sftpUri) {
		DefaultSftpSessionFactory sessionFactory = new DefaultSftpSessionFactory();
		sessionFactory.setHost(sftpUri.getHost());
		sessionFactory.setPort(sftpUri.getPort());

		String[] credentials = sftpUri.getUserInfo().split(":");

		sessionFactory.setUser(credentials[0]);
		sessionFactory.setPassword(credentials.length > 1 ? credentials[1] : null);
		sessionFactory.setAllowUnknownKeys(true);

		return sessionFactory;
	}

	private URI buildUriFromEnvironment(URI providedUri, String resource) throws URISyntaxException {
		if (username == null) {
			throw new RuntimeException("No username was found in: " + providedUri + " nor in sftp_username environment variable");
		}

		if (password == null) {
			throw new RuntimeException("No password was found in: " + providedUri + " nor in sftp_password environment variable");
		}

		return new URI(new StringBuilder()
					.append(SFTP_URI_PREFIX)
					.append(username)
					.append(":")
					.append(password)
					.append("@")
					.append(resource)
					.toString());
	}

	private static class FileFetcher implements InputStreamCallback {
		private byte[] bytes;

		@Override
		public void doWithInputStream(InputStream inputStream) {
			try {
				bytes = IOUtils.toByteArray(inputStream);
			}
			catch (Exception e) {
				throw new RuntimeException("Failed to convert InputStream to byte array", e);
			}
		}

		public byte[] getBytes() {
			return bytes;
		}
	}
}
