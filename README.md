🧑‍💼 Clone do LinkedIn – Projeto Fullstack
Este é um projeto Fullstack que replica funcionalidades essenciais da rede social LinkedIn, desenvolvido com Spring Boot (Java) no backend e ReactJS no frontend.

O objetivo é construir uma plataforma profissional com recursos como conexões entre usuários, postagens, interações e perfis personalizados, aplicando boas práticas modernas de desenvolvimento web.

🚀 Tecnologias Utilizadas
🔧 Backend (Java + Spring Boot)
Spring Boot (Web, Security, JPA)

OAuth2 e JWT para autenticação segura

Spring Data JPA com MySQL

Bean Validation

Mailhog + JavaMail (verificação de e-mail)

Docker (containerização)

💻 Frontend (ReactJS)
ReactJS

Axios (requisições HTTP)

React Router DOM (navegação SPA)

Styled Components ou TailwindCSS

Context API (gerenciamento de estado)

📁 Estrutura do Projeto
php
Copiar
Editar
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

1. Clonar o repositório
bash
Copiar
Editar
git clone https://github.com/seu-usuario/linkedin-clone.git
cd linkedin-clone

3. Backend (Spring Boot)
bash
Copiar
Editar
cd backend
./mvnw spring-boot:run
Certifique-se de ter o MySQL rodando e configure application.properties com suas credenciais.

4. Frontend (React)
bash
Copiar
Editar
cd frontend
npm install
npm run dev
A aplicação será iniciada em http://localhost:5173 (ou porta equivalente).
