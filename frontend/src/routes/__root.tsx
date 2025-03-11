// src/routes/__root.tsx
import { QueryClient, QueryClientProvider } from "@tanstack/react-query"; //Opcional, para usar react-query no futuro
import { Outlet, RootRoute, Router } from "@tanstack/react-router";
import { ChamadoHistoricoRoute } from "./chamadosHistorico";
import { chatchamadodashRoute } from "./chatchamadodash";
import { CodigoDeVerificacaoRoute } from "./codigoDeVerificacao";
import { CriarChamadoRoute } from "./criarChamados";
import {
  dashboardAdminRoute,
  dashboardRoute,
  dashboardClienteRoute,
} from "./dashboard"; // Import the unified dashboard route
import { landingRoute } from "./landingPage";
import { loginRoute } from "./login";
import { meuschatsclientesRoute } from "./meuschatsclientes";
import { RecuperarSenhaRoute } from "./recuperarSenha";
import { TrocarSenhaRoute } from "./trocarSenha";

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
  RecuperarSenhaRoute,
  TrocarSenhaRoute,
  CodigoDeVerificacaoRoute,
  CriarChamadoRoute,
  ChamadoHistoricoRoute,
  meuschatsclientesRoute,
  chatchamadodashRoute,
  dashboardClienteRoute,
  dashboardRoute,
  dashboardAdminRoute,
]);
// Cria o roteador
export const router = new Router({ routeTree });

// Declara o módulo para o TypeScript (importante!)
declare module "@tanstack/react-router" {
  interface Register {
    router: typeof router;
  }
}
