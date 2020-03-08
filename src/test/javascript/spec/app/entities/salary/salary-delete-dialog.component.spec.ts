import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TestTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { SalaryDeleteDialogComponent } from 'app/entities/salary/salary-delete-dialog.component';
import { SalaryService } from 'app/entities/salary/salary.service';

describe('Component Tests', () => {
  describe('Salary Management Delete Component', () => {
    let comp: SalaryDeleteDialogComponent;
    let fixture: ComponentFixture<SalaryDeleteDialogComponent>;
    let service: SalaryService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TestTestModule],
        declarations: [SalaryDeleteDialogComponent]
      })
        .overrideTemplate(SalaryDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SalaryDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SalaryService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
