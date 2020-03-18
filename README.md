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
      - Arquillian 
      - JUnit e Mockito;


# Começando
1. Dependências (instalação e execução):
- Java,
- Maven, 
- Git
	
2.	Processo de Execução:
    
    >Abrir o <b>prompt de comando</b> e digitar o(s) trecho(s) de código conforme indicado: 

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

- Você pode testar utilizando o swagger-ui embutido em: http://localhost:8080/swagger-ui, No campo <b>"Explore"</b> coloque <b>"/openapi"</b> e tecle "Enter" para que a interface carregue a api.
<br/>

- Ou ainda qualquer outra ferramenta de testes para requisições http como <b><i>curl</i></b> ou <b><i>postman</i></b>
<br/>
<br/>

## Utilização e Exemplos principais Recursos:
---
##### CADASTRAR - [POST - /api/movie]	
> <code>
> curl -X POST "http://localhost:8080/api/movie" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"producers\":[\"Prod 1\"],\"studios\":[\"Studio A\",\"Studio B\"],\"title\":\"Movie Test 1\",\"winner\":true,\"year\":2020}"
</code>

##### OBTER POR ID - [GET - /api/movie/{id}] 
> <code> curl -X GET "http://localhost:8080/api/movie/197" -H "accept: application/json"
</code>

##### DELETAR - [DELETE - /api/movie/{id}]
> <code> curl -X DELETE "http://localhost:8080/api/movie/197" -H "accept: application/json"
</code>

##### OBTER INTERVALO - [GET - /api/producer/rangeawards]

> <code>curl -X GET "http://localhost:8080/api/producer/rangeawards" -H "accept: application/json"
</code>

<br/>
<br/>

# Build e Testes
   Para realizar o build e testes basta seguir os passos abaixo:

<i>Build:</i>

	mvn clean package

# Somente os testes
   Para rodar os testes, basta seguir os passos abaixo:
	
	mvn verify 
   
      