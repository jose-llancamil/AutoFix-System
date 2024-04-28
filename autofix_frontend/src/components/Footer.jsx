import React from 'react';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';

const Footer = () => {
  return (
    <Box
      component="footer"
      sx={{
        backgroundColor: '#333',
        color: '#fff',
        padding: '20px',
        textAlign: 'center',
        position: 'fixed',
        bottom: '0',
        width: '100%',
      }}
    >
      <Typography variant="body2">
        &copy; {new Date().getFullYear()} AutoFix. All rights reserved.
      </Typography>
    </Box>
  );
};

export default Footer;
