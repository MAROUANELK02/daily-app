import {Component, OnInit} from '@angular/core';
import Chart from 'chart.js/auto';
import {TasksRepositoryService} from "../services/tasks.repository.service";
import {AppStateService} from "../services/app-state.service";

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrl: './statistics.component.css'
})
export class StatisticsComponent implements OnInit{
  public chart: any;
  public selectedDuration: number = 7;

  constructor(private taskService : TasksRepositoryService,
              private appState : AppStateService) {
  }

  ngOnInit(): void {
    if(this.appState.authState.isAuthenticated) {
      this.appState.getCurrentUserImage();
    }
    this.fetchStatistics(this.selectedDuration);
  }

  fetchStatistics(duration: number): void {
    this.taskService.fetchCompletedStatistics(this.appState.authState.id, duration).subscribe(
      (next) => this.taskService.fetchInProgressStatistics(this.appState.authState.id, duration).subscribe(
        (next) => this.createChart()
      )
    );
  }

  onSearchClick(): void {
    this.fetchStatistics(this.selectedDuration);
  }

  createChart() {
    if (this.chart) {
      this.chart.destroy();
    }

    const labels = Array.from(this.appState.statistics.keys());
    const inProgressLabels = Array.from(this.appState.inProgressStatistics.keys());

    const allLabels = Array.from(new Set([...labels, ...inProgressLabels])).sort();

    const mappedData = allLabels.map(label => this.appState.statistics.get(label) || 0);
    const mappedInProgressData = allLabels.map(label => this.appState.inProgressStatistics.get(label) || 0);

    this.chart = new Chart("MyChart", {
      type: 'line',
      data: {
        labels: allLabels,
        datasets: [
          {
            label: 'Complétées',
            data: mappedData,
            backgroundColor: 'blue',
          },
          {
            label: 'En cours',
            data: mappedInProgressData,
            backgroundColor: 'red',
          }
        ]
      },
      options: {
        aspectRatio: 2.5,
      }
    });
  }
}
