import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import HomePage from '../pages/Home/HomePage';
import Dashboard from '../pages/Dashboard/Dashboard';
import Login from '../pages/Login/Login';
import UserProfile from '../pages/UserProfile/UserProfile';
import Header from '../components/layout/Header';

const AppRoutes = () => {
  return (
    <BrowserRouter>
      <Header /> {/* Header pode ficar aqui se for comum a todas as páginas */}
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/login" element={<Login />} />
        <Route path="/userprofile" element={<UserProfile />} />
      </Routes>
    </BrowserRouter>
  );
};

export default AppRoutes;