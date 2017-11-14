This project provides an `SFTP` backed Spring `Resource` (`SftpResource`) implementation and associated `ResourceLoader` `BeanPostProcessor` for registration.

# Build

```
$ mvn clean install
```

# Usage:

Include the JAR in your POM, i.e.:

```
<dependency>
    <groupId>io.spring.resource</groupId>
    <artifactId>sftp</artifactId>
    <version>1.0.0</version>
</dependency>
```

The `Resource` location format is similar to other's such as FTP using `UrlResource`, for example:

```
sftp:user:pass@some.host.com:6666/filename.csv
```

This string can be passed into typical `getResource(..)` or `setResource(..)` methods utilizing Spring's Resource abstraction to obtain the resource from SFTP.

For security reasons, if you prefer not to pass the username and password in the resource identifier, you must set the following environment variables:

* sftp_username - the SFTP username to use
* sftp_password - the SFTP password to use

The `Resource` location format would then be:

```
sftp:some.host.com:6666/filename.csv
```

# NOTE:

POC code, only implements basic functionality of obtaining a `Resource` from SFTP

