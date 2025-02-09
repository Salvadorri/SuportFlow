// src/routes/__root.tsx
import { RootRoute, Outlet, Router } from "@tanstack/react-router";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query"; //Opcional, para usar react-query no futuro
import { landingRoute } from "./landingPage";
import { loginRoute } from "./login";
import { dashboardRoute } from "./dashboard";
import { RecuperarSenhaRoute } from "./recuperarSenha";
import { TrocarSenhaRoute } from "./trocarSenha";
import { CodigoDeVerificacaoRoute } from "./codigoDeVerificacao";
import { ChamadosRoute } from "./chamados";
import { CriarChamadoRoute } from "./criarChamados";

// Cria um cliente para react-query (opcional, mas útil para gerenciamento de dados)
const queryClient = new QueryClient();

// Define a rota raiz (layout principal)
export const rootRoute = new RootRoute({
  component: () => (
    <>
      {/* Envolve o Outlet/aplicação com QueryClientProvider se for usar react-query*/}
      <QueryClientProvider client={queryClient}>
        <Outlet />
      </QueryClientProvider>
    </>
  ),
});

//Cria o route tree
const routeTree = rootRoute.addChildren([
  landingRoute,
  loginRoute,
  dashboardRoute,
  RecuperarSenhaRoute,
  TrocarSenhaRoute,
  CodigoDeVerificacaoRoute,
  ChamadosRoute,
  CriarChamadoRoute,
]);
// Cria o roteador
export const router = new Router({ routeTree });

// Declara o módulo para o TypeScript (importante!)
declare module "@tanstack/react-router" {
  interface Register {
    router: typeof router;
  }
}
