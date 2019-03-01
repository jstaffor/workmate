import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { ENUM_Language } from '../models/enums/enum-language';

@Injectable()
export class SessionService {
    constructor(private translate: TranslateService) { }

    VAR_TOKEN = 'token';
    VAR_LANGUAGE = 'language';
    VAR_LANGUAGE_DEFAULT = ENUM_Language.ENGLISH.toString();

    setToken(user: string, password: string) {
        sessionStorage.setItem(this.VAR_TOKEN, btoa(user + ':' + password));
    }

    getEncryptedToken() {
        return sessionStorage.getItem(this.VAR_TOKEN);
    }

    getUnencryptedUser() {
        let unEncyptedtoken = atob(sessionStorage.getItem(this.VAR_TOKEN));
        var index = unEncyptedtoken.indexOf(":");  
        var name = unEncyptedtoken.substr(0, index); 
        return name;
    }
    
    removeToken() {
        sessionStorage.removeItem(this.VAR_TOKEN);
    }
    
    setLanguage(language: ENUM_Language) {
        this.translate.use(language.toString());
        sessionStorage.setItem(this.VAR_LANGUAGE, language.toString());
    }

    getLanguage() {
        let language = sessionStorage.getItem(this.VAR_LANGUAGE);
        if(language === null || language === ''){
            language = this.VAR_LANGUAGE_DEFAULT;
        }
        return language;
    }
}