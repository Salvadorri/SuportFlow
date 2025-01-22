import React, { useState } from "react";
import "./styles/App.css";
import Footer from "./components/layout/Footer.jsx";
import HeaderHome from "./components/layout/HeaderHome.jsx";
import Banner from "./components/layout/banner.jsx";
import About from "./components/layout/About.jsx";
import Features from "./components/layout/Features.jsx";
import Contact from './components/layout/Contact.jsx'


function App() {
  const [count, setCount] = useState(0); // Você não está usando count no momento, pode remover se não for necessário depois

  return (
    <div className="app">
      <HeaderHome />
      <Banner />
      <About />
      <Features />
      <Contact/>
      <Footer />
    </div>
  );
}

export default App;
