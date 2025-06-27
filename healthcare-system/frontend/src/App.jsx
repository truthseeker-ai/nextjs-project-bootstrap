import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Layout from './components/Layout';
import PrivateRoute from './components/PrivateRoute';

// Public Pages
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';

// Doctor Pages
import DoctorDashboard from './pages/doctor/Dashboard';
import DoctorProfile from './pages/doctor/Profile';
import ManageSchedule from './pages/doctor/ManageSchedule';
import DaySchedule from './pages/doctor/DaySchedule';

// Patient Pages
import PatientDashboard from './pages/patient/Dashboard';
import PatientProfile from './pages/patient/Profile';
import BookAppointment from './pages/patient/BookAppointment';

function App() {
  return (
    <Router>
      <AuthProvider>
        <Layout>
          <Routes>
            {/* Public Routes */}
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            {/* Doctor Routes */}
            <Route
              path="/doctor/dashboard"
              element={
                <PrivateRoute allowedRoles={['DOCTOR']}>
                  <DoctorDashboard />
                </PrivateRoute>
              }
            />
            <Route
              path="/doctor/profile"
              element={
                <PrivateRoute allowedRoles={['DOCTOR']}>
                  <DoctorProfile />
                </PrivateRoute>
              }
            />
            <Route
              path="/doctor/schedule"
              element={
                <PrivateRoute allowedRoles={['DOCTOR']}>
                  <ManageSchedule />
                </PrivateRoute>
              }
            />
            <Route
              path="/doctor/schedule/:dayOfWeek"
              element={
                <PrivateRoute allowedRoles={['DOCTOR']}>
                  <DaySchedule />
                </PrivateRoute>
              }
            />

            {/* Patient Routes */}
            <Route
              path="/patient/dashboard"
              element={
                <PrivateRoute allowedRoles={['PATIENT']}>
                  <PatientDashboard />
                </PrivateRoute>
              }
            />
            <Route
              path="/patient/profile"
              element={
                <PrivateRoute allowedRoles={['PATIENT']}>
                  <PatientProfile />
                </PrivateRoute>
              }
            />
            <Route
              path="/patient/book-appointment"
              element={
                <PrivateRoute allowedRoles={['PATIENT']}>
                  <BookAppointment />
                </PrivateRoute>
              }
            />

            {/* Redirect unknown routes to home */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </Layout>
      </AuthProvider>
    </Router>
  );
}

export default App;
