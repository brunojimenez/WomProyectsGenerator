
# Proyectos nuevos

## ACL
## Batch
## Consumer
## Rest 
## Rest Producer

# Proyectos antiguos

# POM
Actualizar versión de Springboot
	https://sormuras.github.io/blog/2018-09-13-junit-4-core-vs-jupiter-api.html
	< 2.4 (Junit)
	> 2.5 (Jupiter)
	Ojala 2.7.4 o en su defecto 2.6.12
	
Regularizar properties (java.versin, proyect.build, proyect.reporting)
No tenga versiones en duro (al menos con properties
Usar logging por defecto (quitar log4j)
Incluir Jacoco

# Properties
- Configurar actuator si corresponde
	
	'''management.endpoints.web.base-path=/actuator
	management.endpoints.web.exposure.include=info, healtht, mappings
	management.endpoint.health.show-details=always'''

# General
- Actualizar .gitignore
- Quitar archivos
 - trustore.jks, keystore.jks
 - log4j-spring.xml
- Agregar backlog-spring.xml
- Agregar filter (interceptor) que está en los templates

# Java
- Usar annotation @SLF4J con log.info(), en vez de logger.info()
- Preferir @Value("${prop.key:defaultValue}") en vez de Environment.getProperty("prop.key")
- Preferir @AllArgsConstructor o constructor en vez de @Autowired
- Preferir log.info("[methodName] key={}", value); en vez de log.info("key=" + value);
