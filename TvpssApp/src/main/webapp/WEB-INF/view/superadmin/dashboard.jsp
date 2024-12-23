<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored = "false" %>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Super Admin Dashboard</title>
    <link rel="stylesheet" href="/TvpssApp/resources/css/superAdminDashboard.css">
	<style>
		.stats {
		    display: flex;
		    gap: 20px;
		    margin-bottom: 20px;
		}
		
		.stat {
		    flex: 1;
		    background: #FFFFFF; /* White background for stat cards */
		    text-align: center;
		    padding: 20px;
		    border-radius: 10px;
		    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Subtle shadow */
		}
		
		.stat h3 {
		    font-size: 1em;
		    color: #6B7280; /* Neutral gray for stat labels */
		}
		
		.stat p {
		    font-size: 2em;
		    font-weight: bold;
		    color: #4B6CB7; /* Blue for stat numbers */
		}
		
		.chart-container {
		    background: #FFFFFF; /* White background */
		    border-radius: 10px; /* Rounded corners */
		    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Subtle shadow */
		    padding: 20px; /* Add space inside the container */
		    flex: 1; /* Flex property to allow equal width */
		}
		
		.charts {
		    display: flex;
		    gap: 20px; /* Space between the charts */
		    margin-bottom: 20px; /* Space below the section */
		}
		
		.chart h3 {
		    font-size: 1.1em;
		    color: #4B6CB7; /* Blue for the title */
		    margin-bottom: 10px; /* Space below the title */
		    text-align: center;
		}
		
		.circle-chart {
		    width: 150px;
		    height: 150px;
		    border-radius: 50%;
		    background: conic-gradient(#4B6CB7 80%, #F7F9FF 0%);
		    display: flex;
		    justify-content: center;
		    align-items: center;
		    margin: 20px auto; /* Center align the circle chart */
		}
		
		.circle-chart span {
		    font-size: 1.5em;
		    font-weight: bold;
		    color: #FFFFFF;
		}
		
		.chart p {
		    text-align: center; /* Center-align the text */
		    color: #4B5563; /* Neutral gray for the text */
		    font-size: 0.9em;
		    margin-top: 10px; /* Add spacing above the text */
		}
	</style>
</head>

<body>
    <div class="dashboard">
        <!-- Sidebar -->
        <aside class="sidebar">
            <div class="logo">
                <img src="/TvpssApp/resources/images/TvpssLogo.png" alt="TVPSS Logo">

                <h2>TVPSS</h2>
            </div>
            <nav>
                <ul>
                    <li><a href="#" class="active">Dashboard</a></li>
                    <li><a href="/TvpssApp/manageUsers">User Management</a></li>
                </ul>
            </nav>
            <div class="settings">
			    <div class="setting-item">
			        <i class="icon-settings"></i> Setting
			    </div>
			    <div class="divider"></div>
			    <div class="setting-item">
		            <i class="icon-logout"></i>
		            <a href="/TvpssApp/login" style="text-decoration: none; color: inherit;">Logout</a>
		        </div>
			</div>
        </aside>

        <!-- Main Content -->
        <main class="content">
        
            <header class="header">
			    <div class="header-top">
				    <div class="notification">
				        <div class="notification-icon">
				            <i class="icon-bell"></i>
				            <span class="notification-badge"></span>
				        </div>
				    </div>
					<div class="user-info">
					    <img src="/TvpssApp/resources/images/superAdminLogo.png" alt="User Avatar">
					    <span>User<br>Super Admin</span>
					</div>
			    </div>
			    
			    <div class="divider"></div>

			    <div class="welcome-search">
			        <h1>Hello, welcome!</h1>
			        <div class="search-bar">
			            <input type="text" placeholder="Search here...">
			        </div>
			    </div>
			</header>

            <section class="stats">
                <div class="stat">
                    <h3>Number of State Admin</h3>
                    <p>63</p>
                </div>
                <div class="stat">
                    <h3>Number of PPD Admin</h3>
                    <p>85</p>
                </div>
                <div class="stat">
                    <h3>Number of School Admin</h3>
                    <p>102</p>
                </div>
            </section>
            
            <section class="charts">
			    <div class="chart-container">
			        <div class="chart">
			            <h3>Number of active users</h3>
			            <canvas id="activeUsersChart"></canvas>
			        </div>
			    </div>
			    <div class="chart-container">
			        <div class="chart">
					    <h3>User in Term</h3>
					    <div class="circle-chart">
					        <span>80%</span>
					    </div>
					    <p>Percentage of active users</p>
					</div>
			    </div>
			</section>
        </main>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script>
    document.addEventListener('DOMContentLoaded', () => {
        const barChart = document.getElementById('activeUsersChart').getContext('2d');
        new Chart(barChart, {
            type: 'bar',
            data: {
                labels: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
                datasets: [{
                    label: 'Active Users',
                    data: [12000, 18000, 5000, 10000, 15000, 20000, 22000],
                    backgroundColor: [
                        '#4B6CB7', // Blue for Monday
                        '#4B6CB7', // Blue for Tuesday
                        '#4B6CB7', // Blue for Wednesday
                        '#4B6CB7', // Blue for Thursday
                        '#4B6CB7', // Blue for Friday
                        '#4B6CB7', // Blue for Saturday
                        '#4B6CB7'  // Blue for Sunday
                    ],
                    borderColor: '#354A9F', // Darker blue border
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: {
                    legend: {
                        display: true,
                        labels: {
                            color: '#4B5563' // Neutral gray for the legend text
                        }
                    }
                },
                scales: {
                    x: {
                        ticks: {
                            color: '#4B5563' // Neutral gray for x-axis labels
                        }
                    },
                    y: {
                        beginAtZero: true,
                        ticks: {
                            color: '#4B5563' // Neutral gray for y-axis labels
                        }
                    }
                }
            }
        });

        // Circular chart configuration
        const circleChart = document.querySelector('.circle-chart');
        const percentageSpan = circleChart.querySelector('span');
        const percentage = 80; // Example percentage value

        circleChart.style.background = `conic-gradient(#4B6CB7 ${percentage}%, #F7F9FF ${percentage}%)`;
        percentageSpan.textContent = `${percentage}%`;
    });
    </script>
    
</body>
</html>
