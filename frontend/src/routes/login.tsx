import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import LoginPage from "../pages/login";

export const loginRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/login",
  component: LoginPage,
});
