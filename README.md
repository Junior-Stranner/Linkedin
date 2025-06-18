# 🧑‍💼 Clone do LinkedIn - Projeto Fullstack

Este é um projeto Fullstack que replica funcionalidades essenciais do LinkedIn, desenvolvido com **Spring Boot (Java)** no backend e **ReactJS** no frontend. A ideia é criar uma rede social profissional, com foco em conexões, publicações, perfis e interações, além de aplicar na prática boas práticas de desenvolvimento web moderno.

---

## 🚀 Tecnologias Utilizadas

### 🔧 Backend (Java + Spring Boot)
- Spring Web
- Spring Security + Oauth2
- Spring Data JPA
- JWT (Autenticação)
- MySQL
- Bean Validation
- Docker
- -Mailhog/JavMail

### 💻 Frontend (React)
- ReactJS
- Axios
- React Router DOM
- Styled Components / TailwindCSS (a depender)
- Context API

---

## 📁 Estrutura do Projeto

📦linkedin-clone/
┣ 📂backend/
┃ ┣ 📂controller
┃ ┣ 📂service
┃ ┣ 📂model
┃ ┗ 📂repository
┣ 📂frontend/
┃ ┣ 📂components
┃ ┣ 📂pages
┃ ┗ 📂services

---

## 🔐 Funcionalidades

✅ Autenticação com JWT  
✅ Cadastro e login de usuários  
✅ Visualização e edição de perfil  
✅ Conexão entre usuários  
✅ Criação de postagens  
✅ Comentários e curtidas  
✅ Upload de imagens de perfil e posts  

---

## 📦 Como rodar o projeto

### Backend
```bash
cd backend
./mvnw spring-boot:run
Frontend
bash
Copiar
Editar
cd frontend
npm install
npm run dev
