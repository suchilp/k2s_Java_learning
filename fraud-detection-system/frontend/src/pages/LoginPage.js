import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import PropTypes from 'prop-types';
import {
  Container,
  Box,
  TextField,
  Button,
  Card,
  Typography,
  Alert,
  CircularProgress,
} from '@mui/material';
import { authAPI } from '../api/client';
import { login } from '../store/authSlice';

const VALIDATION_RULES = {
  username: {
    minLength: 3,
    maxLength: 50,
    pattern: /^[a-zA-Z0-9._-]+$/,
  },
  password: {
    minLength: 3,
    maxLength: 100,
  },
};

export const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState({});
  const [apiError, setApiError] = useState('');
  const [loading, setLoading] = useState(false);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const validateForm = () => {
    const newErrors = {};

    if (!username) {
      newErrors.username = 'Username is required';
    } else if (username.length < VALIDATION_RULES.username.minLength) {
      newErrors.username = 'Username must be at least 3 characters';
    } else if (username.length > VALIDATION_RULES.username.maxLength) {
      newErrors.username = 'Username cannot exceed 50 characters';
    } else if (!VALIDATION_RULES.username.pattern.test(username)) {
      newErrors.username = 'Username can only contain letters, numbers, dots, hyphens, and underscores';
    }

    if (!password) {
      newErrors.password = 'Password is required';
    } else if (password.length < VALIDATION_RULES.password.minLength) {
      newErrors.password = 'Password must be at least 3 characters';
    } else if (password.length > VALIDATION_RULES.password.maxLength) {
      newErrors.password = 'Password cannot exceed 100 characters';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setApiError('');

    if (!validateForm()) {
      return;
    }

    setLoading(true);

    try {
      const response = await authAPI.login(username, password);
      dispatch(
        login({
          user: response.data.user || { username },
          token: response.data.token || 'sample-token',
        })
      );
      navigate('/dashboard');
    } catch (err) {
      const errorMessage = err.response?.data?.message || err.message || 'Login failed. Please try again.';
      setApiError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="sm">
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          minHeight: '100vh',
        }}
      >
        <Card sx={{ padding: 4, width: '100%' }}>
          <Typography variant="h4" sx={{ mb: 1, textAlign: 'center', fontWeight: 'bold' }}>
            🏦 Banking Fraud Detection
          </Typography>
          <Typography variant="body2" sx={{ mb: 3, textAlign: 'center', color: 'textSecondary' }}>
            Online Transaction Monitoring System
          </Typography>

          {apiError && (
            <Alert severity="error" sx={{ mb: 2 }} role="alert">
              {apiError}
            </Alert>
          )}

          <form onSubmit={handleLogin} noValidate>
            <TextField
              fullWidth
              label="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              margin="normal"
              placeholder="admin"
              error={!!errors.username}
              helperText={errors.username}
              disabled={loading}
              inputProps={{
                'aria-label': 'Username input',
                autoComplete: 'username',
              }}
            />
            <TextField
              fullWidth
              type="password"
              label="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              margin="normal"
              placeholder="••••••••"
              error={!!errors.password}
              helperText={errors.password}
              disabled={loading}
              inputProps={{
                'aria-label': 'Password input',
                autoComplete: 'current-password',
              }}
            />
            <Button
              fullWidth
              variant="contained"
              color="primary"
              type="submit"
              sx={{ mt: 3, py: 1.5 }}
              disabled={loading}
              aria-busy={loading}
            >
              {loading ? (
                <>
                  <CircularProgress size={20} sx={{ mr: 1 }} />
                  Logging in...
                </>
              ) : (
                'Login'
              )}
            </Button>
          </form>

          <Alert severity="info" sx={{ mt: 3 }}>
            <Typography variant="caption">
              <strong>Demo Credentials:</strong> username: <code>admin</code>, password: <code>admin</code>
            </Typography>
          </Alert>
        </Card>
      </Box>
    </Container>
  );
};

LoginPage.propTypes = {};
