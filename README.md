🧑‍💼 Clone do LinkedIn — Projeto Fullstack
Este é um projeto Fullstack inspirado no LinkedIn, que replica funcionalidades essenciais da rede social profissional.
Foi desenvolvido com Spring Boot (Java) no backend e ReactJS no frontend, seguindo boas práticas modernas de desenvolvimento web.

O objetivo é criar uma plataforma profissional com:
Conexões entre usuários
Postagens
Interações sociais
Perfis personalizados

🚀 Tecnologias Utilizadas
🔧 Backend (Java + Spring Boot)
Spring Boot (Web, Security, JPA)
OAuth2 + JWT (Autenticação segura)
Spring Data JPA + MySQL
Bean Validation
JavaMail + Mailhog (Verificação de e-mail)
Docker (Containerização)


💻 Frontend (ReactJS)
ReactJS

Axios (Requisições HTTP)
React Router DOM (Navegação SPA)
Styled Components ou TailwindCSS
Context API (Gerenciamento de estado)


📁 Estrutura do Projeto

linkedin-clone/
├── backend/
│   ├── controller/
│   ├── service/
│   ├── model/
│   └── repository/
├── frontend/
│   ├── components/
│   ├── pages/
│   └── services/

🔐 Funcionalidades Implementadas
✅ Cadastro e login de usuários
✅ Autenticação e autorização com JWT
✅ Verificação de e-mail
✅ Visualização e edição de perfil
✅ Upload de imagem de perfil e capa
✅ Conexão entre usuários
✅ Criação e exibição de postagens
✅ Curtidas e comentários

▶️ Como Executar o Projeto
1️⃣ Clonar o repositório

git clone https://github.com/seu-usuario/linkedin-clone.git
cd linkedin-clone

2️⃣ Rodar o Backend (Spring Boot)
cd backend
./mvnw spring-boot:run
💡 Certifique-se de ter o MySQL rodando e o application.properties devidamente configurado.

3️⃣ Rodar o Frontend (React)
cd frontend
npm install
npm run dev
💡 A aplicação será iniciada em: http://localhost:5173
