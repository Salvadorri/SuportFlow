# Imagem base com Node.js LTS
FROM node:22-alpine

# Diretório de trabalho
WORKDIR /app

# Copia os arquivos de dependência (primeiro para cache eficiente)
COPY package*.json pnpm-lock.yaml ./

# Instala as dependências usando pnpm (instale globalmente se não estiver presente)
RUN npm install -g pnpm && pnpm install --frozen-lockfile

# Copie o código-fonte do frontend
COPY / .

# Expondo a porta padrão do Vite
EXPOSE 3000

# Comando para iniciar o servidor de desenvolvimento
CMD ["pnpm", "run", "dev"]