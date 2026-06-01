import axios from 'axios';

// API Configuration:
// - For development: Auth Service directly on 8081
// - For production: API Gateway on 8080 (requires gateway to be built)
const API_BASE = process.env.REACT_APP_API_URL || 'http://localhost:8081';

// Create axios instance with default config
const apiClient = axios.create({
  baseURL: API_BASE,
  timeout: 10000,
});

// Request interceptor - Add auth token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor - Handle errors
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authAPI = {
  login: (username, password) =>
    apiClient.post('/auth/login', { username, password }),
  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },
};

// Transaction API
export const transactionAPI = {
  createTransaction: (transaction) =>
    apiClient.post('/transaction/transfer', transaction),
  getTransactions: () =>
    apiClient.get('/transaction/list'),
};

// Fraud API
export const fraudAPI = {
  evaluateFraud: (transactionData) =>
    apiClient.post('/fraud/evaluate', transactionData),
  getAlerts: () =>
    apiClient.get('/fraud/alerts'),
};

// User API
export const userAPI = {
  getProfile: () =>
    apiClient.get('/user/profile'),
};

// Audit API
export const auditAPI = {
  getLogs: () =>
    apiClient.get('/audit/logs'),
};
