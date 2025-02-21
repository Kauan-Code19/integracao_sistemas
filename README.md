# traduzzo_api
Api do sistema traduzzo

# Arquivos de propriedades
Para uma melhor separação dos ambientes, crie dois arquivos
chamados application-development.properties e application-production.properties
dentro da pasta resources.

Caso você queira utilizar um dos arquivos criados, será necessário
definir isso no arquivo principal application.properties,
escolhendo um perfil e configurando: spring.profiles.active=nome_do_perfil.

Todos os atributos que possuem a anotação @Value("nome_da_propriedade")
devem ser definidos em um arquivo de propriedade.