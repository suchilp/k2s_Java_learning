import axios from 'axios';

const API_BASE = 'http://localhost:8080';

// Auth API
export const authAPI = {
  login: (username, password) =>
    axios.post(`${API_BASE}/auth/login`, { username, password }),
  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },
};

// Transaction API
export const transactionAPI = {
  createTransaction: (transaction) =>
    axios.post(`${API_BASE}/transaction/transfer`, transaction, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
    }),
  getTransactions: () =>
    axios.get(`${API_BASE}/transaction/list`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
    }),
};

// Fraud API
export const fraudAPI = {
  evaluateFraud: (transactionData) =>
    axios.post(`${API_BASE}/fraud/evaluate`, transactionData, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
    }),
  getAlerts: () =>
    axios.get(`${API_BASE}/fraud/alerts`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
    }),
};

// User API
export const userAPI = {
  getProfile: () =>
    axios.get(`${API_BASE}/user/profile`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
    }),
};

// Audit API
export const auditAPI = {
  getLogs: () =>
    axios.get(`${API_BASE}/audit/logs`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
    }),
};
