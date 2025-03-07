
```
# main

* [java/](./main/java)
  * [com/](./main/java/com)
    * [suportflow/](./main/java/com/suportflow)
      * [backend/](./main/java/com/suportflow/backend)
        * [config/](./main/java/com/suportflow/backend/config)
          * [DataInitializer.java](./main/java/com/suportflow/backend/config/DataInitializer.java)
          * [PasswordEncoderConfig.java](./main/java/com/suportflow/backend/config/PasswordEncoderConfig.java)
          * [SecurityConfig.java](./main/java/com/suportflow/backend/config/SecurityConfig.java)
        * [controller/](./main/java/com/suportflow/backend/controller)
          * [AuthController.java](./main/java/com/suportflow/backend/controller/AuthController.java)
          * [UserController.java](./main/java/com/suportflow/backend/controller/UserController.java)
        * [dto/](./main/java/com/suportflow/backend/dto)
          * [AuthenticationRequest.java](./main/java/com/suportflow/backend/dto/AuthenticationRequest.java)
          * [AuthenticationResponse.java](./main/java/com/suportflow/backend/dto/AuthenticationResponse.java)
          * [RefreshTokenDTO.java](./main/java/com/suportflow/backend/dto/RefreshTokenDTO.java)
          * [UserDetailsDTO.java](./main/java/com/suportflow/backend/dto/UserDetailsDTO.java)
          * [UserLoginDTO.java](./main/java/com/suportflow/backend/dto/UserLoginDTO.java)
          * [UserRegistrationDTO.java](./main/java/com/suportflow/backend/dto/UserRegistrationDTO.java)
        * [exception/](./main/java/com/suportflow/backend/exception)
          * [GlobalExceptionHandler.java](./main/java/com/suportflow/backend/exception/GlobalExceptionHandler.java)
          * [TokenRefreshException.java](./main/java/com/suportflow/backend/exception/TokenRefreshException.java)
          * [UserNotFoundException.java](./main/java/com/suportflow/backend/exception/UserNotFoundException.java)
        * [model/](./main/java/com/suportflow/backend/model)
          * [Empresa.java](./main/java/com/suportflow/backend/model/Empresa.java)
          * [Permissao.java](./main/java/com/suportflow/backend/model/Permissao.java)
          * [RefreshToken.java](./main/java/com/suportflow/backend/model/RefreshToken.java)
          * [User.java](./main/java/com/suportflow/backend/model/User.java)
        * [repository/](./main/java/com/suportflow/backend/repository)
          * [EmpresaRepository.java](./main/java/com/suportflow/backend/repository/EmpresaRepository.java)
          * [PermissaoRepository.java](./main/java/com/suportflow/backend/repository/PermissaoRepository.java)
          * [RefreshTokenRepository.java](./main/java/com/suportflow/backend/repository/RefreshTokenRepository.java)
          * [UserRepository.java](./main/java/com/suportflow/backend/repository/UserRepository.java)
        * [security/](./main/java/com/suportflow/backend/security)
          * [JwtAuthenticationFilter.java](./main/java/com/suportflow/backend/security/JwtAuthenticationFilter.java)
          * [JwtUtil.java](./main/java/com/suportflow/backend/security/JwtUtil.java)
        * [service/](./main/java/com/suportflow/backend/service)
          * [auth/](./main/java/com/suportflow/backend/service/auth)
            * [AuthenticationService.java](./main/java/com/suportflow/backend/service/auth/AuthenticationService.java)
            * [EmpresaService.java](./main/java/com/suportflow/backend/service/auth/EmpresaService.java)
            * [RefreshTokenService.java](./main/java/com/suportflow/backend/service/auth/RefreshTokenService.java)
            * [UserDetailsServiceImpl.java](./main/java/com/suportflow/backend/service/auth/UserDetailsServiceImpl.java)
          * [cliente/](./main/java/com/suportflow/backend/service/cliente)
          * [user/](./main/java/com/suportflow/backend/service/user)
            * [UserManagementService.java](./main/java/com/suportflow/backend/service/user/UserManagementService.java)
        * [util/](./main/java/com/suportflow/backend/util)
          * [RequestLoggingMiddleware.java](./main/java/com/suportflow/backend/util/RequestLoggingMiddleware.java)
        * [BackendApplication.java](./main/java/com/suportflow/backend/BackendApplication.java)
* [resources/](./main/resources)
  * [db/](./main/resources/db)
    * [migration/](./main/resources/db/migration)
      * [V1__criar_tabelas_e_indices.sql](./main/resources/db/migration/V1__criar_tabelas_e_indices.sql)
  * [application-dev.yml](./main/resources/application-dev.yml)
  * [application-test.yml](./main/resources/application-test.yml)
  * [application.yml](./main/resources/application.yml)
```


1. config/ (Configuração):

DataInitializer.java: Implementa a interface ApplicationRunner para executar código na inicialização da aplicação. Neste caso, ele cria a empresa "SuportFlow", as permissões e um usuário administrador se eles não existirem no banco de dados.

PasswordEncoderConfig.java: (Provavelmente) Define um bean PasswordEncoder (como BCryptPasswordEncoder) que é usado para criptografar senhas. É referenciado no SecurityConfig e UserService.

SecurityConfig.java: Classe central de configuração do Spring Security. Define como a autenticação e a autorização são gerenciadas. Configura o AuthenticationManager, define as regras de acesso aos endpoints, especifica o filtro JWT (JwtAuthenticationFilter) e o UserDetailsService (CustomUserDetailsService).

2. controller/ (Controladores):

AuthController.java: Lida com as requisições relacionadas à autenticação, como login (/api/auth/login) e registro (/api/auth/register). Ele usa o AuthenticationManager para autenticar usuários e o JwtUtil para gerar tokens JWT.

EmpresaController.java: Lida com as requisições relacionadas a empresas (por exemplo, /api/empresas). Fornece endpoints para criar, buscar, atualizar e excluir empresas. Usa o EmpresaService para a lógica de negócio.

3. dto/ (Data Transfer Objects):

AuthenticationRequest.java: Representa os dados da requisição de login (normalmente email e senha).

AuthenticationResponse.java: Representa a resposta do login, contendo o token JWT.

UserDetailsDTO.java, UserLoginDTO.java, UserRegistrationDTO.java: São classes simples (POJOs - Plain Old Java Objects) usadas para transferir dados entre as camadas da aplicação, especialmente entre o cliente (front-end) e o controller. Podem ser usadas para transitar dados do User (ainda não implementadas nesse projeto).

4. exception/ (Exceções):

GlobalExceptionHandler.java: Um manipulador global de exceções que captura exceções lançadas pela aplicação e retorna respostas de erro formatadas para o cliente.

UserNotFoundException.java: Uma exceção personalizada que é lançada quando um usuário não é encontrado.

5. model/ (Modelo):

CustomUserDetails.java: Implementa a interface UserDetails do Spring Security. É uma classe auxiliar usada pelo Spring Security para representar um usuário autenticado. Ela encapsula um objeto User e fornece informações como as permissões do usuário.

Empresa.java: Entidade JPA que representa a tabela empresas no banco de dados.

Permissao.java: Entidade JPA que representa a tabela permissoes no banco de dados.

User.java: Entidade JPA que representa a tabela usuarios no banco de dados.

6. repository/ (Repositórios):

EmpresaRepository.java: Interface que estende JpaRepository e fornece métodos para interagir com a tabela empresas (buscar, salvar, atualizar, excluir).

PermissaoRepository.java: Interface que estende JpaRepository e fornece métodos para interagir com a tabela permissoes.

UserRepository.java: Interface que estende JpaRepository e fornece métodos para interagir com a tabela usuarios.

7. security/ (Segurança):

JwtAuthenticationFilter.java: Um filtro do Spring Security que intercepta as requisições, extrai o token JWT do cabeçalho Authorization, valida o token e, se for válido, autentica o usuário.

JwtUtil.java: Uma classe utilitária que fornece métodos para gerar, validar e extrair informações de tokens JWT.

8. service/ (Serviços):

auth/: Subpacote para serviços relacionados à autenticação.

CustomUserDetailsService.java: Implementa a interface UserDetailsService do Spring Security. É responsável por carregar os detalhes do usuário (incluindo as permissões) com base no email, usado durante o processo de autenticação.

EmpresaService.java: Contém a lógica de negócio relacionada a empresas. Fornece métodos para criar, buscar, atualizar e excluir empresas. Usa o EmpresaRepository para interagir com o banco de dados.

UserService.java: Contém a lógica de negócio relacionada a usuários, como registrar novos usuários e buscar usuários pelo email. Usa o UserRepository para interagir com o banco de dados.

9. util/ (Utilitários):

Vazio no seu exemplo. Geralmente usado para classes utilitárias genéricas que não se encaixam em outras categorias.

10. BackendApplication.java:

A classe principal da aplicação Spring Boot. Contém o método main que inicia a aplicação. A anotação @SpringBootApplication habilita a autoconfiguração do Spring Boot e o component scan.

Relacionamento entre os Componentes:

Requisição do Cliente: O cliente (por exemplo, um aplicativo front-end) envia uma requisição HTTP para um endpoint da API (por exemplo, /api/auth/login, /api/empresas).

Controller: O Controller correspondente (AuthController ou EmpresaController) recebe a requisição.

DTO: O Controller desserializa os dados da requisição em um objeto DTO (por exemplo, AuthenticationRequest).

Service: O Controller chama o Service apropriado (UserService, EmpresaService ou CustomUserDetailsService) para processar a requisição.

Repository: O Service usa o Repository correspondente (UserRepository, EmpresaRepository ou PermissaoRepository) para interagir com o banco de dados (buscar, salvar, atualizar ou excluir dados).

Model: Os Repositorys retornam entidades JPA (User, Empresa, Permissao) que representam os dados do banco de dados.

Security (Autenticação):

Se a requisição for para /api/auth/login, o AuthController usa o AuthenticationManager (configurado no SecurityConfig) para autenticar o usuário.

O AuthenticationManager usa o CustomUserDetailsService para carregar os detalhes do usuário.

Se a autenticação for bem-sucedida, o AuthController usa o JwtUtil para gerar um token JWT.

O token JWT é retornado para o cliente em um AuthenticationResponse.

Security (Autorização):

Para requisições a endpoints protegidos, o JwtAuthenticationFilter intercepta a requisição.

Ele extrai o token JWT do cabeçalho Authorization.

Usa o JwtUtil para validar o token.

Se o token for válido, usa o CustomUserDetailsService para carregar os detalhes do usuário e define o usuário autenticado no SecurityContextHolder.

Service (Lógica de Negócio): O Service executa a lógica de negócio necessária.

Controller (Resposta): O Controller serializa os dados de retorno (normalmente uma entidade ou um DTO) em JSON e retorna a resposta para o cliente.

Exception: Se ocorrer uma exceção em qualquer ponto do fluxo, o GlobalExceptionHandler captura a exceção e retorna uma resposta de erro formatada para o cliente.

Em resumo, o fluxo de uma requisição em uma aplicação Spring Boot típica segue os seguintes passos:

Requisição HTTP -> Controller -> Service -> Repository -> Banco de Dados -> Repository -> Service -> Controller -> Resposta HTTP

Camadas:

Controller: Camada de apresentação, lida com as requisições HTTP.

Service: Camada de serviço, contém a lógica de negócio.

Repository: Camada de acesso a dados, interage com o banco de dados.

Model: Representa as entidades do domínio (do negócio).

DTO: Objetos simples para transferência de dados entre camadas.

Security: Lida com autenticação e autorização.

Config: Classes de configuração do Spring.

Exception: Lida com exceções e formatação de erros.

Essa estrutura ajuda a organizar o código, separar as responsabilidades e tornar a aplicação mais fácil de manter e testar.
