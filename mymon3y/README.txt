O GarbageCollector do Java mantem referência quando um classloader carrega uma classe dinamicamente. Para evitar um estouro de memória no uso da aplicação, inicialize o tomcat com os seguintes parâmetros:

-XX:+CMSClassUnloadingEnabled -XX:+CMSPermGenSweepingEnabled

Ainda, o Apache Expression Language, utilizado pelo JSF, automaticamente converte campos nulos em zero. Esta propriedade impede algumas verificações no MyMon3y. Para desativá-la, insira no seguinte arquivo do Tomcat:

conf/catalina.properties

A linha:

org.apache.el.parser.COERCE_TO_ZERO=false

O projeto faz uso do banco de dados MySQL. Você precisa executar as seguintes linhas como administrador para configurá-lo:

DROP DATABASE IF EXISTS mymon3y;
DROP USER mymon3y;
CREATE DATABASE mymon3y;
CREATE USER mymon3y IDENTIFIED BY 'mymon3y';
GRANT ALL PRIVILEGES ON mymon3y.* TO mymon3y@"%";