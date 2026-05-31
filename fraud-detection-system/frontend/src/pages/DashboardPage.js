import React, { useState } from 'react';
import PropTypes from 'prop-types';
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
  CircularProgress,
  Chip,
} from '@mui/material';
import { transactionAPI, fraudAPI } from '../api/client';

const AMOUNT_VALIDATION = {
  min: 0.01,
  max: 1000000,
};

DashboardPage.propTypes = {};

export const DashboardPage = () => {
  const [amount, setAmount] = useState('');
  const [merchant, setMerchant] = useState('');
  const [description, setDescription] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [fraudResult, setFraudResult] = useState(null);
  const [validationErrors, setValidationErrors] = useState({});

  const validateForm = () => {
    const newErrors = {};

    if (!amount) {
      newErrors.amount = 'Amount is required';
    } else {
      const amountNum = parseFloat(amount);
      if (isNaN(amountNum)) {
        newErrors.amount = 'Amount must be a valid number';
      } else if (amountNum < AMOUNT_VALIDATION.min) {
        newErrors.amount = `Minimum amount is $${AMOUNT_VALIDATION.min}`;
      } else if (amountNum > AMOUNT_VALIDATION.max) {
        newErrors.amount = `Maximum amount is $${AMOUNT_VALIDATION.max.toLocaleString()}`;
      }
    }

    if (!merchant || merchant.trim().length === 0) {
      newErrors.merchant = 'Merchant is required';
    } else if (merchant.length > 100) {
      newErrors.merchant = 'Merchant name cannot exceed 100 characters';
    }

    if (description && description.length > 500) {
      newErrors.description = 'Description cannot exceed 500 characters';
    }

    setValidationErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmitTransaction = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setFraudResult(null);

    if (!validateForm()) {
      return;
    }

    setLoading(true);

    try {
      const transactionData = {
        userId: 'user-' + Date.now(),
        amount: parseFloat(amount),
        currency: 'USD',
        merchant: merchant.trim(),
        description: description.trim() || 'Transfer',
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
      setSuccess('✅ Transaction submitted and fraud evaluated!');
      setAmount('');
      setMerchant('');
      setDescription('');
      setValidationErrors({});
    } catch (err) {
      const errorMessage = err.response?.data?.message || err.message || 'Failed to process transaction';
      setError(errorMessage);
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

            <form onSubmit={handleSubmitTransaction} noValidate>
              <TextField
                fullWidth
                label="Amount (USD)"
                type="number"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                margin="normal"
                placeholder="1000.00"
                error={!!validationErrors.amount}
                helperText={validationErrors.amount}
                disabled={loading}
                inputProps={{
                  step: '0.01',
                  min: '0.01',
                  max: '1000000',
                  'aria-label': 'Transaction amount',
                }}
              />
              <TextField
                fullWidth
                label="Merchant *"
                value={merchant}
                onChange={(e) => setMerchant(e.target.value)}
                margin="normal"
                placeholder="Amazon, Walmart, etc."
                error={!!validationErrors.merchant}
                helperText={validationErrors.merchant}
                disabled={loading}
                inputProps={{
                  'aria-label': 'Merchant name',
                  maxLength: 100,
                }}
              />
              <TextField
                fullWidth
                label="Description (Optional)"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                margin="normal"
                placeholder="Optional transaction description"
                error={!!validationErrors.description}
                helperText={validationErrors.description}
                disabled={loading}
                inputProps={{
                  'aria-label': 'Transaction description',
                  maxLength: 500,
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
                    Processing...
                  </>
                ) : (
                  'Submit Transaction'
                )}
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
                  📊 Submit a transaction to see fraud evaluation
                </Typography>
              </Paper>
            ) : (
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <Paper sx={{ p: 2, bgcolor: '#e3f2fd' }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <Box>
                      <Typography variant="body2" color="textSecondary">
                        Fraud Score
                      </Typography>
                      <Typography variant="h5" sx={{ color: '#1976d2', fontWeight: 'bold' }}>
                        {fraudResult.fraudScore || fraudResult.score || 'N/A'}%
                      </Typography>
                    </Box>
                  </Box>
                </Paper>

                <Paper sx={{ p: 2, bgcolor: '#fce4ec' }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <Box>
                      <Typography variant="body2" color="textSecondary">
                        Risk Level
                      </Typography>
                      <Chip
                        label={fraudResult.riskLevel || 'UNKNOWN'}
                        color={fraudResult.riskLevel === 'HIGH' ? 'error' : fraudResult.riskLevel === 'MEDIUM' ? 'warning' : 'success'}
                        variant="filled"
                        sx={{ mt: 1 }}
                      />
                    </Box>
                  </Box>
                </Paper>

                <Paper sx={{ p: 2, bgcolor: '#f3e5f5' }}>
                  <Typography variant="body2" color="textSecondary">
                    Transaction Status
                  </Typography>
                  <Chip
                    label={fraudResult.status || 'PENDING'}
                    sx={{
                      mt: 1,
                      backgroundColor: fraudResult.status === 'APPROVED' ? '#c8e6c9' : fraudResult.status === 'BLOCKED' ? '#ffcdd2' : '#fff9c4',
                    }}
                  />
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
