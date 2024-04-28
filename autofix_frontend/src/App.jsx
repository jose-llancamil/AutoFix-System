import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from "./components/Navbar";
import Home from './components/Home';
import VehicleList from './components/VehicleList';
import AddEditVehicle from './components/AddEditVehicle';
import RepairList from './components/RepairList';
import AddEditRepair from './components/AddEditRepair';
import BonusList from './components/BonusList';
import AddEditBonus from './components/AddEditBonus';
import RepairCostsReport from './components/RepairCostsReport'; // R1
import RepairTypeSummaryReport from './components/RepairTypeSummaryReport'; // R2
import AverageRepairTimeReport from './components/AverageRepairTimeReport'; // R3
import RepairTypesEngineReport from './components/RepairTypesEngineReport'; // R4
import NotFound from './components/NotFound';
import Footer from './components/Footer';

function App() {
  return (
    <Router>
      <div className="container">
        <Navbar /> 
        
        <Routes> 
          <Route path="/" element={<Home />} />
          <Route path="/vehicles" element={<VehicleList />} />
          <Route path="/vehicles/create" element={<AddEditVehicle />} />
          <Route path="/vehicles/edit/:id" element={<AddEditVehicle />} />

          <Route path="/repairs" element={<RepairList />} />
          <Route path="/repairs/create" element={<AddEditRepair />} />
          <Route path="/repairs/edit/:id" element={<AddEditRepair />} />

          <Route path="/bonuses" element={<BonusList />} />
          <Route path="/bonuses/create" element={<AddEditBonus />} />
          <Route path="/bonuses/edit/:id" element={<AddEditBonus />} />

          <Route path="/reports/repair-costs" element={<RepairCostsReport />} />
          <Route path="/reports/repair-type-summary" element={<RepairTypeSummaryReport />} />
          <Route path="/reports/average-repair-time" element={<AverageRepairTimeReport />} />
          <Route path="/reports/repair-types-engine-summary" element={<RepairTypesEngineReport />} />

          <Route path="*" element={<NotFound />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;