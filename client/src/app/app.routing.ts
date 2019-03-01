import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { AdminMenuComponent } from './components/admin/admin-menu.component';
import { CompanyComponent } from './components/admin/company/company.component';
import { AuthGuard } from './helpers/auth-guard-helper';

const appRoutes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
    { path: 'admin/menu', component: AdminMenuComponent,  canActivate: [AuthGuard],  data: {role: 'Admin'}},
    { path: 'admin/menu/company', component: CompanyComponent,  canActivate: [AuthGuard],  data: {role: 'Admin'} },
    { path: '**', redirectTo: 'login' }
];

export const routing = RouterModule.forRoot(appRoutes);