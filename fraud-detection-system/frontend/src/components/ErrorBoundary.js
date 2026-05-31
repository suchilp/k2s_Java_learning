import React from 'react';
import PropTypes from 'prop-types';
import { Box, Container, Typography, Button } from '@mui/material';

class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error) {
    return { hasError: true, error };
  }

  componentDidCatch(error, errorInfo) {
    console.error('Error caught by boundary:', error, errorInfo);
  }

  handleReset = () => {
    this.setState({ hasError: false, error: null });
  };

  render() {
    if (this.state.hasError) {
      return (
        <Container maxWidth="sm">
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              minHeight: '100vh',
              flexDirection: 'column',
            }}
          >
            <Typography variant="h4" sx={{ mb: 2, color: 'error.main' }}>
              ⚠️ Something went wrong
            </Typography>
            <Typography variant="body1" sx={{ mb: 3, textAlign: 'center', color: 'textSecondary' }}>
              {this.state.error?.message || 'An unexpected error occurred. Please try again.'}
            </Typography>
            <Button variant="contained" color="primary" onClick={this.handleReset}>
              Try Again
            </Button>
          </Box>
        </Container>
      );
    }

    return this.props.children;
  }
}

ErrorBoundary.propTypes = {
  children: PropTypes.node.isRequired,
};

export default ErrorBoundary;
