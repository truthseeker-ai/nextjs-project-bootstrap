import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function Navbar() {
  const { user, logout } = useAuth();

  return (
    <nav className="bg-white shadow-lg">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex">
            <div className="flex-shrink-0 flex items-center">
              <Link to="/" className="text-xl font-bold text-gray-800">
                Healthcare Portal
              </Link>
            </div>
          </div>

          <div className="flex items-center">
            {user ? (
              <div className="flex items-center space-x-4">
                <span className="text-gray-700">
                  Welcome, {user.role === 'DOCTOR' ? 'Dr.' : ''} {user.name}
                </span>
                <Link
                  to={user.role === 'DOCTOR' ? '/doctor/dashboard' : '/patient/dashboard'}
                  className="text-gray-600 hover:text-gray-900"
                >
                  Dashboard
                </Link>
                {user.role === 'PATIENT' && (
                  <Link
                    to="/patient/book-appointment"
                    className="text-gray-600 hover:text-gray-900"
                  >
                    Book Appointment
                  </Link>
                )}
                <button
                  onClick={logout}
                  className="ml-4 px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700"
                >
                  Logout
                </button>
              </div>
            ) : (
              <div className="space-x-4">
                <Link
                  to="/login"
                  className="text-gray-600 hover:text-gray-900"
                >
                  Login
                </Link>
                <Link
                  to="/register"
                  className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700"
                >
                  Register
                </Link>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
