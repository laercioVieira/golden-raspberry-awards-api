# golden-raspberry-awards-api

# Introdução
Aplicativo com API para cadastro, exclusão e consulta de filmes e produtores.

O projeto utiliza tecnologias e bibliotecas do ecossistema Java ;)

      - Java 8;
      - Microprofile;
      - Thorntail;
      - JAX-RS;
      - JPA (hibernate);
      - CDI;
      - Data Base H2 (In Memory DataBase);
      - MicroProfile-OpenApi;
      - Jboss Logging;
      - Arquillian e Mockito;


# Começando
1. Dependências (instalação e execução):
- Java,
- Maven, 
- Git
	
2.	Processo de Execução:

    Baixar o projeto:

    <code>
    git clone https://github.com/laercioVieira/golden-raspberry-awards-api.git
    </code>
    
    Você pode executar com o Maven: <br/>
    <code>
      mvn thorntail:run
    </code>

    Ou Rodar diretamente:
  </br> 
   Para isso execute o arquivo "run.bat" (para windows) ou "run.sh" (linux);

    Após isso a aplicação (Api) estará disponivel sob a contexto em: http://localhost:8080/api

    Você pode testar utilizando o swagger-ui embutido em: http://localhost:8080/swagger-ui, No campo <b>"Explore"</b> coloque <b>"/openapi"</b> para que o interface carregue a api.
<br/>

      - Ou ainda qualquer outra ferramenta como para requisições http curl ou postman

# Build e Testes
   Para realizar o build e testes basta seguir os passos abaixo:
	
	mvn clean package

# Somente os testes
   Para rodar os testes, basta seguir os passos abaixo:
	
	mvn verify
