# traduzzo_api
Api do sistema traduzzo

# Arquivos de propriedades
Para separar os ambientes de forma mais organizada, crie três arquivos dentro
da pasta resources:

application.properties
application-development.properties
application-production.properties

Por padrão, se nenhum perfil for definido, o arquivo principal será o
application.properties.

Se desejar utilizar um dos arquivos específicos, defina o perfil no arquivo
principal, configurando a seguinte propriedade:
spring.profiles.active=nome_do_perfil

Além disso, todos os atributos anotados com @Value("nome_da_propriedade") devem
estar definidos em um dos arquivos de propriedades.