O GarbageCollector do Java mantem referências quando um classloader carrega uma classe dinamicamente. Para evitar um estouro de memória no uso da aplicação, inicialize o tomcat com os seguintes parâmetros:

-XX:+CMSClassUnloadingEnabled -XX:+CMSPermGenSweepingEnabled

Ainda, o Apache Expression Language, utilizado pelo JSF, automaticamente converte campos nulos em zero. Esta propriedade impede algumas verificações no MyMon3y. Para desativá-la, insira no seguinte arquivo do Tomcat:

conf/catalina.properties

A linha:

org.apache.el.parser.COERCE_TO_ZERO=false

O projeto faz uso do banco de dados MySQL. Você precisa executar os seguintes comandos como administrador para configurá-lo:

DROP DATABASE IF EXISTS mymon3y;
DROP USER mymon3y;
CREATE DATABASE mymon3y;
CREATE USER mymon3y IDENTIFIED BY 'mymon3y';
GRANT ALL PRIVILEGES ON mymon3y.* TO mymon3y@"%";

Em seguida, para que as tabelas sejam geradas, execute a seguinte task do ant: bd-gerar-banco-mysql

OBS: Caso algum erro ocorra e as tabelas não sejam geradas, tente repetir o processo.
Caso ainda assim as tabelas não sejam geradas, por favor entrar em contato com os desenvolvedores.