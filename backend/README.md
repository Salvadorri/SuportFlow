Para arquivos nos chamados:
Componente de Upload: Use um componente de upload de arquivos (HTML `<input type="file">` e JavaScript) para permitir que os usuários selecionem os arquivos.
Envio do Arquivo: Envie o arquivo para o endpoint /upload no seu backend usando uma requisição POST com Content-Type: multipart/form-data.
Exibição do Arquivo: Receba o caminho do arquivo do backend e exiba-o na interface do chat. Você pode criar um link para o arquivo ou exibir a imagem diretamente (se for uma imagem).

Todo: Arrumar o put de função
