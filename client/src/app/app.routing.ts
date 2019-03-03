import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { AdminMenuComponent } from './components/admin/admin-menu.component';
import { CompanyDialogComponent } from './components/admin/companies/company/company.component';
import { CompaniesComponent } from './components/admin/companies/companies.component';
import { AuthGuard } from './helpers/auth-guard-helper';
import { ENUM_Role } from './models/enums/enum-role';

const appRoutes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
    { path: 'admin/menu', component: AdminMenuComponent,  canActivate: [AuthGuard],  data: {role: ENUM_Role.ADMIN.toString()}},
    { path: 'admin/company', component: CompanyDialogComponent,  canActivate: [AuthGuard],  data: {role: ENUM_Role.ADMIN.toString()} },
    { path: 'admin/companies', component: CompaniesComponent,  canActivate: [AuthGuard],  data: {role: ENUM_Role.ADMIN.toString()} },
    { path: '**', redirectTo: 'login' }
];

export const routing = RouterModule.forRoot(appRoutes);