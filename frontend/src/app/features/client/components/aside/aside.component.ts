import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../auth/services/auth.service';
import { ChatService } from '../../../booking/services/chat.service';
import { ChatMessage } from '../../../booking/models/chat-message.model';

@Component({
  selector: 'app-aside',
  imports: [CommonModule, RouterModule],
  templateUrl: './aside.component.html',
  styleUrl: './aside.component.scss',
})
export class AsideComponent implements OnInit {
    private readonly authService = inject(AuthService);
    private readonly router = inject(Router);
    private readonly chatService = inject(ChatService);
    recentMessages: ChatMessage[] = [];
    errorMessage = '';

    ngOnInit(): void {
      this.chatService.getRecentMessages().subscribe({
        next: (messages) => (this.recentMessages = messages),
        error: (err) => (this.errorMessage = err.message),
      });
    }

    logout(){
    this.authService.logout();
    this.router.navigate(['/'])
  }

}
