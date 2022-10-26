![Wom](images/wom.png?raw=true "Wom")

# Proyectos nuevos

## ACL
Servicio REST con una capa de Resilence4J.

## Batch
Servicio Spring Batch.
- Ejemplo MongoDB
- Ejemplo RestTemplate
- Ejemplo RetryTemplate

## Consumer
Servicio consumidor de Kafka.
- Ejemplo MongoDB
- Ejemplo RestTemplate
- Ejemplo RetryTemplate

## Rest 
Servicio Spring Web.
- Ejemplo MongoDB
- Ejemplo RestTemplate
- Ejemplo RetryTemplate
- Ejemplo Test MockMVC
- Ejemplo Test Service

## Rest Producer
Servicio Spring Web con producción de mensajes a Kafka.
- Ejemplo MongoDB
- Ejemplo RestTemplate
- Ejemplo RetryTemplate
- Ejemplo Test MockMVC
- Ejemplo Test Service

# Proyectos antiguos

# POM
- Actualizar versión de Springboot (2.7.4 o en su defecto 2.6.12)
  - https://sormuras.github.io/blog/2018-09-13-junit-4-core-vs-jupiter-api.html
  - < 2.4 (Junit)
  - > 2.5 (Jupiter)

```
<parent>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-parent</artifactId>
	<version>2.6.12</version>
	<relativePath /> <!-- lookup parent from repository -->
</parent>
```
	
- Regularizar properties
  - coverage.ratio=0.4 proyectos antiguos
  - coverage.ratio=0.8 proyectos nuevos
  
```
<properties>
	<java.version>11</java.version>
	<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
	<coverage.ratio>0.8</coverage.ratio>
	<jacoco.version>0.8.8</jacoco.version>
</properties>
```

- Revisar que no tenga versiones en duro (al menos la versión configurada como propiedad)
- Usar logging por defecto (quitar log4j)
- Incluir Jacoco y configurar exclusiones.

```
<build>
	<plugins>
		<plugin>
			<groupId>org.jacoco</groupId>
			<artifactId>jacoco-maven-plugin</artifactId>
			<version>${jacoco.version}</version>
			<configuration>
				<excludes>
					<exclude>**cl/wom/api/config/**</exclude>
					<exclude>**cl/wom/api/exception/**</exclude>
					<exclude>*Test</exclude>
				</excludes>
			</configuration>
			<executions>
				<execution>
					<goals>
						<goal>prepare-agent</goal>
					</goals>
				</execution>
				<execution>
					<id>jacoco-report</id>
					<phase>prepare-package</phase>
					<goals>
						<goal>report</goal>
					</goals>
					<configuration>
						<outputDirectory>target/jacoco-report</outputDirectory>
					</configuration>
				</execution>
				<execution>
					<id>jacoco-check</id>
					<goals>
						<goal>check</goal>
					</goals>
					<configuration>
						<rules>
							<rule>
								<element>BUNDLE</element>
								<limits>
									<limit>
										<counter>LINE</counter>
										<value>COVEREDRATIO</value>
										<minimum>${coverage.ratio}</minimum>
									</limit>
								</limits>
							</rule>
						</rules>
					</configuration>
				</execution>
			</executions>
		</plugin>
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-surefire-plugin</artifactId>
			<configuration>
				<systemPropertyVariables>
					<jacoco-agent.destfile>target/jacoco.exec</jacoco-agent.destfile>
				</systemPropertyVariables>
			</configuration>
		</plugin>
	</plugins>
</build>
```

# Properties

- Configurar actuator si corresponde

```	
management.endpoints.web.base-path=/actuator
management.endpoints.web.exposure.include=info, healtht, mappings
management.endpoint.health.show-details=always
```

# General
- Actualizar .gitignore
- Quitar archivos remanentes
  - trustore.jks
  - keystore.jks
  - log4j-spring.xml
- Agregar
  - backlog-spring.xml
- Agregar filter (interceptor) a los proyectos web.

# Java
- Usar annotation @SLF4J con log.info(), en vez de logger.info()
- Preferir @Value("${prop.key:defaultValue}") en vez de Environment.getProperty("prop.key")
- Preferir @AllArgsConstructor o constructor en vez de @Autowired
- No usar log.info("[methodName] key={}", value); el methodName se incluye de forma automática en el log.
