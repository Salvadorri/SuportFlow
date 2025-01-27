import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import HomePage from '../pages/HomePage.jsx';


const AppRoutes = () => {
  return (
    <BrowserRouter>
      <Header /> {/* Header pode ficar aqui se for comum a todas as páginas */}
      <Routes>
        <Route path="/" element={<HomePage />} />
      </Routes>
    </BrowserRouter>
  );
};

export default AppRoutes;