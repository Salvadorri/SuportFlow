import logo from "../../assets/logo.png";
import { Link } from "@tanstack/react-router";

function Index() {
  return (
    <div className="min-h-screen flex flex-col font-sans">
      {/* Header */}
      <header className="bg-gray-800 py-4 shadow-md sticky top-0 z-50">
        <div className="container mx-auto flex items-center justify-between px-4">
          <div className="flex items-center">
            <img
              src={logo}
              alt="SupportFlowAI Logo"
              className="h-8 w-auto mr-2"
            />
            <span className="text-lg font-bold text-white">SupportFlow</span>
          </div>
          {/* Container para os links e botão (Alinhados à direita) */}
          <div className="flex items-center">
            <nav>
              <ul className="flex space-x-4 md:space-x-6">
                {" "}
                {/* Ajuste space-x para telas menores */}
                <li>
                  <a
                    href="#sobre"
                    className="text-white hover:text-green-600 transition"
                  >
                    Sobre
                  </a>
                </li>
                <li>
                  <a
                    href="#recursos"
                    className="text-white hover:text-green-600 transition"
                  >
                    Recursos
                  </a>
                </li>
                <li>
                  <a
                    href="#contato"
                    className="text-white hover:text-green-600 transition"
                  >
                    Contato
                  </a>
                </li>
              </ul>
            </nav>
            <Link
              to="/login"
              className="bg-green-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition ml-4"
            >
              {/* ml-4 adicionado */}
              Login / Signup
            </Link>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="flex-grow">
        {/* Banner */}
        <section className="bg-green-500 text-white py-16 md:py-20 text-center">
          {/* Ajustado py */}
          <div className="container mx-auto px-4">
            <h1 className="text-3xl md:text-4xl font-bold mb-4">
              SupportFlow
            </h1>{" "}
            {/* Ajustado text-xl */}
            <p className="text-base md:text-xl">
              A Solução Completa para Suporte ao Cliente
            </p>{" "}
            {/* Ajustado text-base */}
          </div>
        </section>

        {/* About */}
        <section id="sobre" className="py-12 md:py-16">
          {/* Ajustado py */}
          <div className="container mx-auto px-4">
            <h2 className="text-2xl md:text-3xl font-bold text-center mb-6 md:mb-8">
              Sobre
            </h2>{" "}
            {/* Ajustado text-2xl e mb */}
            <p className="text-gray-200 text-base md:text-lg text-center">
              {/* Ajustado text-base */}
              Lorem ipsum dolor sit amet, consectetur adipisci elit, sed eiusmod
              tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim
              veniam, quis nostrum exercitationem ullam corporis suscipit
              laboriosam, nisi ut aliquid ex ea commodi consequatur. Quis aute
              iure reprehenderit in voluptate velit esse cillum dolore eu fugiat
              nulla pariatur. Excepteur sint obcaecat cupiditat non proident,
              sunt in culpa qui officia deserunt mollit anim id est laborum.
            </p>
          </div>
        </section>

        {/* Features */}
        <section id="recursos" className="py-12 md:py-16 bg-gray-50">
          {/* Ajustado py */}
          <div className="container mx-auto px-4">
            <h2 className="text-gray-700 text-2xl md:text-3xl font-bold text-center mb-8 md:mb-12">
              Recursos
            </h2>{" "}
            {/* Ajustado text-2xl e mb */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 md:gap-8">
              {/* Feature Card 1 */}
              <div className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition flex flex-col justify-center items-center">
                <h3 className="text-gray-700 text-lg md:text-xl font-semibold mb-2 md:mb-4 text-center">
                  Recurso X
                </h3>{" "}
                {/* Ajustado text-lg e mb */}
                <p className="text-gray-700 text-sm md:text-base text-center">
                  {" "}
                  {/* Ajustado text-sm */}
                  Lorem ipsum dolor sit amet, consectetur adipisci elit, sed
                  eiusmod tempor incidunt ut labore et dolore magna aliqua.
                </p>
              </div>
              {/* Feature Card 2 */}
              <div className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition flex flex-col justify-center items-center">
                <h3 className="text-gray-700 text-lg md:text-xl font-semibold mb-2 md:mb-4 text-center">
                  Recurso Y
                </h3>{" "}
                {/* Ajustado text-lg e mb */}
                <p className="text-gray-700 text-sm md:text-base text-center">
                  {" "}
                  {/* Ajustado text-sm */}
                  Lorem ipsum dolor sit amet, consectetur adipisci elit, sed
                  eiusmod tempor incidunt ut labore et dolore magna aliqua.
                </p>
              </div>
              {/* Feature Card 3 */}
              <div className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition flex flex-col justify-center items-center">
                <h3 className="text-gray-700 text-lg md:text-xl font-semibold mb-2 md:mb-4 text-center">
                  Recurso Z
                </h3>{" "}
                {/* Ajustado text-lg e mb */}
                <p className="text-gray-700 text-sm md:text-base text-center">
                  {" "}
                  {/* Ajustado text-sm */}
                  Lorem ipsum dolor sit amet, consectetur adipisci elit, sed
                  eiusmod tempor incidunt ut labore et dolore magna aliqua.
                </p>
              </div>
            </div>
          </div>
        </section>

        {/* Contact */}
        <section id="contato" className="py-12 md:py-16">
          {" "}
          {/* Ajustado py */}
          <div className="container mx-auto px-4">
            <h2 className="text-2xl md:text-3xl font-bold text-center mb-6 md:mb-8">
              Entre em Contato
            </h2>{" "}
            {/* Ajustado text-2xl e mb */}
            <div className="text-center">
              <p className="mb-2 md:mb-4">
                {" "}
                {/* Ajustado mb */}
                <span className="font-semibold">Telefone:</span>
                <a
                  href="tel:+1234567890"
                  className="text-gray-300 hover:text-green-600"
                >
                  {" "}
                  +123 456 7890
                </a>
              </p>
              <p>
                <span className="font-semibold">Email:</span>
                <a
                  href="mailto:contato@suportflowai.com"
                  className="text-gray-300 hover:text-green-600"
                >
                  {" "}
                  contato@suportflow.com
                </a>
              </p>
            </div>
          </div>
        </section>
      </main>

      {/* Footer */}
      <footer className="bg-gray-800 text-white py-6">
        <div className="container mx-auto px-4 text-center">
          <div className="flex flex-col md:flex-row justify-center space-y-2 md:space-y-0 md:space-x-4 mb-4">
            {/* Ajustes para mobile e desktop */}
            <a
              href="#termos"
              className="text-white hover:text-green-600 transition"
            >
              Termos de Uso
            </a>
            <a
              href="#privacidade"
              className="text-white hover:text-green-600 transition"
            >
              Política de Privacidade
            </a>
            <a
              href="#faq"
              className="text-white hover:text-green-600 transition"
            >
              FAQ
            </a>
          </div>
          <p className="text-sm">
            © 2025 SupportFlow. Todos os direitos reservados.
          </p>
        </div>
      </footer>
    </div>
  );
}

export default Index;
