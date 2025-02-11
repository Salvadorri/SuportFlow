import { Route } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import LoginPage from "../pages/login";

export const loginRoute = new Route({
  getParentRoute: () => rootRoute,
  path: "/login",
  component: LoginPage,
});
