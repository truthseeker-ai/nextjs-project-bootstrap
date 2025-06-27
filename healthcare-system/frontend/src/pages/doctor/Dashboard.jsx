import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import axios from 'axios';
import LoadingSpinner from '../../components/LoadingSpinner';
import Alert from '../../components/Alert';

function DoctorDashboard() {
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null);
  const [appointments, setAppointments] = useState([]);
  const [stats, setStats] = useState({
    todayAppointments: 0,
    pendingAppointments: 0,
    totalPatients: 0
  });

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      const [appointmentsRes, statsRes] = await Promise.all([
        axios.get(`/api/appointments/doctor/${user.id}`),
        axios.get(`/api/doctors/${user.id}/stats`)
      ]);

      setAppointments(appointmentsRes.data);
      setStats(statsRes.data);
    } catch (error) {
      setAlert({
        type: 'error',
        message: 'Failed to fetch dashboard data'
      });
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {alert && (
        <Alert
          type={alert.type}
          message={alert.message}
          onClose={() => setAlert(null)}
        />
      )}

      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">
          Welcome, Dr. {user.name}
        </h1>
        <p className="mt-2 text-gray-600">
          Manage your appointments and schedule
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-lg font-medium text-gray-900">Today's Appointments</h3>
          <p className="mt-2 text-3xl font-bold text-indigo-600">
            {stats.todayAppointments}
          </p>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-lg font-medium text-gray-900">Pending Appointments</h3>
          <p className="mt-2 text-3xl font-bold text-yellow-600">
            {stats.pendingAppointments}
          </p>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-lg font-medium text-gray-900">Total Patients</h3>
          <p className="mt-2 text-3xl font-bold text-green-600">
            {stats.totalPatients}
          </p>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-white rounded-lg shadow">
          <div className="p-6">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold text-gray-900">
                Upcoming Appointments
              </h2>
              <Link
                to="/doctor/appointments"
                className="text-indigo-600 hover:text-indigo-800"
              >
                View all
              </Link>
            </div>
            {appointments.length > 0 ? (
              <div className="space-y-4">
                {appointments.slice(0, 5).map((appointment) => (
                  <div
                    key={appointment.id}
                    className="flex items-center justify-between p-4 bg-gray-50 rounded-lg"
                  >
                    <div>
                      <p className="font-medium text-gray-900">
                        {appointment.patientName}
                      </p>
                      <p className="text-sm text-gray-500">
                        {new Date(appointment.dateTime).toLocaleString()}
                      </p>
                    </div>
                    <span
                      className={`px-3 py-1 rounded-full text-sm font-medium ${
                        appointment.status === 'CONFIRMED'
                          ? 'bg-green-100 text-green-800'
                          : 'bg-yellow-100 text-yellow-800'
                      }`}
                    >
                      {appointment.status}
                    </span>
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-gray-500 text-center py-4">
                No upcoming appointments
              </p>
            )}
          </div>
        </div>

        <div className="bg-white rounded-lg shadow">
          <div className="p-6">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold text-gray-900">
                Schedule Management
              </h2>
              <Link
                to="/doctor/schedule"
                className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700"
              >
                Manage Schedule
              </Link>
            </div>
            <div className="space-y-4">
              <div className="p-4 bg-gray-50 rounded-lg">
                <p className="text-gray-600">
                  Set your availability, manage appointments, and configure your working hours.
                </p>
                <div className="mt-4 space-y-2">
                  <Link
                    to="/doctor/schedule/new"
                    className="block text-indigo-600 hover:text-indigo-800"
                  >
                    ➜ Create new schedule
                  </Link>
                  <Link
                    to="/doctor/schedule/view"
                    className="block text-indigo-600 hover:text-indigo-800"
                  >
                    ➜ View current schedule
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default DoctorDashboard;
