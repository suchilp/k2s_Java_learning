import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  Container,
} from '@mui/material';
import { useDispatch } from 'react-redux';
import { logout } from './store/authSlice';
import { LoginPage } from './pages/LoginPage';
import { DashboardPage } from './pages/DashboardPage';
import { PrivateRoute } from './components/PrivateRoute';

function App() {
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
  const user = useSelector((state) => state.auth.user);
  const dispatch = useDispatch();

  const handleLogout = () => {
    dispatch(logout());
  };

  return (
    <BrowserRouter>
      <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
        {/* AppBar */}
        <AppBar position="static">
          <Toolbar>
            <Typography variant="h6" sx={{ flexGrow: 1 }}>
              🏦 Banking Fraud Detection
            </Typography>
            {isAuthenticated && (
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                <Typography variant="body2">
                  {user?.username || 'User'}
                </Typography>
                <Button color="inherit" onClick={handleLogout}>
                  Logout
                </Button>
              </Box>
            )}
          </Toolbar>
        </AppBar>

        {/* Main Content */}
        <Box sx={{ flex: 1 }}>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route
              path="/dashboard"
              element={<PrivateRoute element={<DashboardPage />} />}
            />
            <Route
              path="/"
              element={
                isAuthenticated ? (
                  <Navigate to="/dashboard" />
                ) : (
                  <Navigate to="/login" />
                )
              }
            />
          </Routes>
        </Box>

        {/* Footer */}
        <Box
          sx={{
            bgcolor: '#f5f5f5',
            py: 3,
            textAlign: 'center',
            borderTop: '1px solid #ddd',
          }}
        >
          <Typography variant="body2" color="textSecondary">
            © 2026 Online Banking Fraud Detection System
          </Typography>
          <Typography variant="caption" color="textSecondary">
            Built with React, Material-UI, and Java Microservices
          </Typography>
        </Box>
      </Box>
    </BrowserRouter>
  );
}

export default App;
