# Imagem base com Node.js LTS
FROM node:23-alpine AS build

# Diretório de trabalho
WORKDIR /app

# Copia os arquivos de dependência (primeiro para cache eficiente)

COPY package.json pnpm-lock.yaml ./


# Instala as dependências usando pnpm (instale globalmente se não estiver presente)
RUN npm install -g pnpm && pnpm install --frozen-lockfile

# Copie o código-fonte do frontend
COPY / .

# Construi a pagina
RUN npm run build

# Servidor proxy
FROM caddy:2.9.1-alpine

# Cria o repositorio web
RUN mkdir -p /app/www

# Copia arquivos do build
COPY --from=build /app/dist /app/www

# Copia o Caddyfile
COPY Caddyfile /etc/caddy/Caddyfile

# Expondo as portas (HTTP and HTTPS)
EXPOSE 80
EXPOSE 443