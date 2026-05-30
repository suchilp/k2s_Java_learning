import React, { useState } from 'react';
import {
  Container,
  Box,
  Card,
  TextField,
  Button,
  Typography,
  Alert,
  Grid,
  Paper,
} from '@mui/material';
import { transactionAPI, fraudAPI } from '../api/client';

export const DashboardPage = () => {
  const [amount, setAmount] = useState('');
  const [merchant, setMerchant] = useState('');
  const [description, setDescription] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [fraudResult, setFraudResult] = useState(null);

  const handleSubmitTransaction = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setFraudResult(null);
    setLoading(true);

    try {
      const transactionData = {
        userId: 'user-' + Date.now(),
        amount: parseFloat(amount),
        currency: 'USD',
        merchant: merchant || 'Unknown',
        description: description || 'Transfer',
      };

      // Submit transaction
      const txResponse = await transactionAPI.createTransaction(transactionData);
      const transactionId = txResponse.data.id || txResponse.data.transactionId || 'txn-' + Date.now();

      // Evaluate fraud
      const fraudResponse = await fraudAPI.evaluateFraud({
        transactionId,
        userId: transactionData.userId,
        amount: transactionData.amount,
        merchant: transactionData.merchant,
      });

      setFraudResult(fraudResponse.data);
      setSuccess('Transaction submitted and fraud evaluated!');
      setAmount('');
      setMerchant('');
      setDescription('');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to process transaction');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Typography variant="h4" sx={{ mb: 4 }}>
        Dashboard
      </Typography>

      <Grid container spacing={3}>
        {/* Transaction Form */}
        <Grid item xs={12} md={6}>
          <Card sx={{ p: 3 }}>
            <Typography variant="h6" sx={{ mb: 2 }}>
              Submit Transaction
            </Typography>

            {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
            {success && <Alert severity="success" sx={{ mb: 2 }}>{success}</Alert>}

            <form onSubmit={handleSubmitTransaction}>
              <TextField
                fullWidth
                label="Amount"
                type="number"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                margin="normal"
                placeholder="1000.00"
                inputProps={{ step: '0.01' }}
              />
              <TextField
                fullWidth
                label="Merchant"
                value={merchant}
                onChange={(e) => setMerchant(e.target.value)}
                margin="normal"
                placeholder="Amazon, Walmart, etc."
              />
              <TextField
                fullWidth
                label="Description"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                margin="normal"
                placeholder="Optional description"
              />
              <Button
                fullWidth
                variant="contained"
                color="primary"
                type="submit"
                sx={{ mt: 3 }}
                disabled={loading || !amount}
              >
                {loading ? 'Processing...' : 'Submit Transaction'}
              </Button>
            </form>
          </Card>
        </Grid>

        {/* Fraud Result */}
        <Grid item xs={12} md={6}>
          <Card sx={{ p: 3 }}>
            <Typography variant="h6" sx={{ mb: 2 }}>
              Fraud Evaluation Result
            </Typography>

            {!fraudResult ? (
              <Paper sx={{ p: 2, bgcolor: '#f5f5f5', textAlign: 'center' }}>
                <Typography color="textSecondary">
                  Submit a transaction to see fraud evaluation
                </Typography>
              </Paper>
            ) : (
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <Paper sx={{ p: 2, bgcolor: '#e3f2fd' }}>
                  <Typography variant="body2" color="textSecondary">
                    Fraud Score
                  </Typography>
                  <Typography variant="h5" sx={{ color: '#1976d2' }}>
                    {fraudResult.fraudScore || fraudResult.score || 'N/A'}%
                  </Typography>
                </Paper>

                <Paper sx={{ p: 2, bgcolor: '#fce4ec' }}>
                  <Typography variant="body2" color="textSecondary">
                    Risk Level
                  </Typography>
                  <Typography
                    variant="h5"
                    sx={{
                      color:
                        fraudResult.riskLevel === 'HIGH'
                          ? '#d32f2f'
                          : fraudResult.riskLevel === 'MEDIUM'
                          ? '#f57c00'
                          : '#388e3c',
                    }}
                  >
                    {fraudResult.riskLevel || 'UNKNOWN'}
                  </Typography>
                </Paper>

                <Paper sx={{ p: 2, bgcolor: '#f3e5f5' }}>
                  <Typography variant="body2" color="textSecondary">
                    Status
                  </Typography>
                  <Typography variant="h6" sx={{ mt: 1 }}>
                    {fraudResult.status || 'PENDING'}
                  </Typography>
                </Paper>

                {fraudResult.details && (
                  <Paper sx={{ p: 2, bgcolor: '#fff3e0' }}>
                    <Typography variant="body2" color="textSecondary">
                      Details
                    </Typography>
                    <Typography variant="body2" sx={{ mt: 1 }}>
                      {fraudResult.details}
                    </Typography>
                  </Paper>
                )}
              </Box>
            )}
          </Card>
        </Grid>

        {/* System Info */}
        <Grid item xs={12}>
          <Card sx={{ p: 3, bgcolor: '#f0f4c3' }}>
            <Typography variant="h6" sx={{ mb: 2 }}>
              System Information
            </Typography>
            <Grid container spacing={2}>
              <Grid item xs={12} sm={3}>
                <Box>
                  <Typography variant="body2" color="textSecondary">
                    Auth Service
                  </Typography>
                  <Typography variant="body1">Port 8081</Typography>
                </Box>
              </Grid>
              <Grid item xs={12} sm={3}>
                <Box>
                  <Typography variant="body2" color="textSecondary">
                    Transaction Service
                  </Typography>
                  <Typography variant="body1">Port 8082</Typography>
                </Box>
              </Grid>
              <Grid item xs={12} sm={3}>
                <Box>
                  <Typography variant="body2" color="textSecondary">
                    Fraud Engine
                  </Typography>
                  <Typography variant="body1">Port 8083</Typography>
                </Box>
              </Grid>
              <Grid item xs={12} sm={3}>
                <Box>
                  <Typography variant="body2" color="textSecondary">
                    API Gateway
                  </Typography>
                  <Typography variant="body1">Port 8080</Typography>
                </Box>
              </Grid>
            </Grid>
          </Card>
        </Grid>
      </Grid>
    </Container>
  );
};
