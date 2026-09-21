export const logout = () => {
  localStorage.removeItem('jwt_token');
  window.location.href = '/';
};