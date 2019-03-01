import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { AdminMenuComponent } from './components/admin/admin-menu.component';
import { CompanyComponent } from './components/admin/company/company.component';
import { AuthGuard } from './helpers/auth-guard-helper';
import { ENUM_Role } from './models/enums/enum-role';

const appRoutes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
    { path: 'admin/menu', component: AdminMenuComponent,  canActivate: [AuthGuard],  data: {role: ENUM_Role.ADMIN.toString()}},
    { path: 'admin/menu/company', component: CompanyComponent,  canActivate: [AuthGuard],  data: {role: ENUM_Role.ADMIN.toString()} },
    { path: '**', redirectTo: 'login' }
];

export const routing = RouterModule.forRoot(appRoutes);