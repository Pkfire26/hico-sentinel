package com.hico.controllers;

import com.hico.models.*;
import com.hico.exception.*;
import com.hico.services.CustomUserDetailsService;
import com.hico.repositories.ClubRepository;
import com.hico.repositories.ClubAssociationRepository;
import com.hico.repositories.TeacherRepository;
import com.hico.repositories.UserRepository;
import com.hico.repositories.StudentRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/club")
public class ClubController {
    @Autowired
    private ClubRepository clubs;

    @Autowired
    private UserRepository users;

    @Autowired
    private StudentRepository students;

    @Autowired
    private TeacherRepository teachers;

    @Autowired
    private ClubAssociationRepository clubAssociations;

    @Autowired
    private CustomUserDetailsService userService;

    // list the clubs belonging to user's school
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public HashSet<Club> getAllClubs() {

        User sessionUser = userService.getLoggedInUser();

        return clubs.findAllBySchoolId(sessionUser.getSchoolId());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Club getClubById(@PathVariable("id") String id) {
        return clubs.findById(id).get();
    }

    // get the named club from users' school clubs
    //@RequestMapping(value = "/{name}", method = RequestMethod.GET)
    @RequestMapping(method = RequestMethod.GET)
    public HashSet getClubByName(@RequestParam String name) {
        User sessionUser = userService.getLoggedInUser();
        if (sessionUser.getRoles().contains(new Role(Role.ADMIN))) {
            // List all clubs for a system admin user.
            return clubs.findAllByName(name);
        } else {
            String schoolId = sessionUser.getSchoolId();
            HashSet<Club> clubList = new HashSet<Club>();
            Club club =  clubs.findByNameAndSchoolId(name, schoolId);
            clubList.add(club);
            return clubList;
        }
    }


    // List Club members
    @RequestMapping(value = "/{id}/list", method = RequestMethod.GET)
    public HashSet<ClubMember> getClubMembers(@PathVariable("id") String id) {

        User sessionUser = userService.getLoggedInUser();

        Club club = clubs.findById(id).get();
        if (club == null) {
            throw new RecordNotFoundException("no such club " + id);
        }

        if (!club.getSchoolId().equalsIgnoreCase(sessionUser.getSchoolId())) {
            throw new InsufficentAccessException(
                    "Insufficent access -  member list available only by club associates");
        }

        // check if the user is associated with the club
        ClubAssociation assoc = clubAssociations.findByClubIdAndUserId(id,
                sessionUser.getId());

        if ((!sessionUser.getRoles().contains(new Role(Role.SCHOOL_ADMIN))) &&
               ((assoc == null)  ||
                ((!assoc.getClubRoles().contains(ClubAssociation.CLUB_SUPERVISOR)) &&
                (!assoc.getClubRoles().contains(ClubAssociation.CLUB_ADVISOR)) &&
                (!assoc.getClubRoles().contains(ClubAssociation.CLUB_ADMIN)) &&
                (!assoc.getClubRoles().contains(ClubAssociation.CLUB_MEMBER))))) {

               throw new InsufficentAccessException(
                    "Insufficent access - only school admin and " +
                    " valid club associates can see member list");
        }

        HashSet<ClubAssociation> assocList = clubAssociations.findAllByClubId(club.getId());
        HashSet<ClubMember> memberList = new HashSet<ClubMember>();
        if (assocList == null) {
            throw new RecordNotFoundException("No members for club " + id);
        }
        for (ClubAssociation tassoc : assocList) {
            System.out.println(tassoc);
            User user = users.findById(tassoc.getUserId()).get();
            if (user == null) {
                System.out.println("Inconsistent status in db");
                continue;
            }

            ClubMember member = new ClubMember(user, tassoc.getClubRoles());
            memberList.add(member);
        }
        return memberList;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void modifyClubById(@PathVariable("id") String id, @Valid @RequestBody Club club) {
        /*
        club.setId(id);
        clubs.save(club);
        */
    }

    // Only a school-admin can create a club and add teachers to it.
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Club createClub(@Valid @RequestBody ClubCreate info) {

        User sessionUser = userService.getLoggedInUser();
        if (!sessionUser.getRoles().contains(new Role(Role.SCHOOL_ADMIN))) {
            throw new InsufficentAccessException(
                    "Insufficent access - need to have school admin access");
        }
        String schoolId = sessionUser.getSchoolId();

        Club club = clubs.findByNameAndSchoolId(info.getName(), schoolId);
        if (club != null) {
            throw new RecordAlreadyExistsException("Club with this name already exists");
        }
        club = new Club(info.getName(), schoolId);
        club.setDescription(info.getDescription());
        club = clubs.save(club);


        if (info.getSupervisors() == null || info.getSupervisors().size() == 0) {
            Teacher teacher = teachers.findByUserIdAndSchoolId(sessionUser.getId(),
                    schoolId);
            if (teacher == null) {
                throw new SentinelServerInternalError(
                        "Inconsitent state - no teacher record for userId: " +
                        sessionUser.getId());
            }
            // add the session user as the supervisor
            ClubAssociation assoc = new ClubAssociation(club.getId(),
                    sessionUser.getId());
            System.out.println(assoc);
            assoc.addRole(ClubAssociation.CLUB_SUPERVISOR);
            assoc = clubAssociations.save(assoc);
            //teacher.addClubAssociation(assoc.getId());
            //teachers.save(teacher);
            System.out.println("Update association for teacher club-supervisor " +
                    sessionUser.getEmailId());
        } else {
            // club registration contains email's of teachers who have not yet
            // registered with the system.
            for (String emailId : info.getSupervisors()) {
                User user = users.findByEmailId(emailId);
                Teacher teacher;
                if (user == null) {
                    // user does not exist - create one for registration.
                    user = new User(emailId, "", "", "", schoolId, Role.TEACHER);
                    // fix this to be random and email should contain the temporary password
                    String encodedPassword = new BCryptPasswordEncoder().encode("hico123");
                    user.setPassword(encodedPassword);
                    user = users.save(user);
                    teacher = new Teacher(user.getId(), schoolId);
                } else {
                    // club registration contains email of teachers who are already
                    // registered.
                    teacher = teachers.findByUserIdAndSchoolId(user.getId(),
                            schoolId);
                    if (teacher == null) {
                        throw new SentinelServerInternalError(
                                "Inconsitent state - no teacher record for userId: " +
                                user.getId());
                    }
                }
                // create the club association and save associations
                ClubAssociation assoc = new ClubAssociation(club.getId(), user.getId());
                assoc.addRole(ClubAssociation.CLUB_SUPERVISOR);
                assoc = clubAssociations.save(assoc);
                //teacher.addClubAssociation(assoc.getId());
                //teachers.save(teacher);
                System.out.println("Update association for teacher club-supervisor " +
                        user.getEmailId());
            }
        }


        // registration contains email's for student club admins
        for (String emailId : info.getClubAdmins()) {
            User user = users.findByEmailId(emailId);
            Student student;
            if (user == null) {
                // user does not exist - create one for registration.
                user = new User(emailId, "", "", "", schoolId,
                        Role.STUDENT);
                // fix this to be random and email should contain the temporary password
                String encodedPassword = new BCryptPasswordEncoder().encode("hico123");
                user.setPassword(encodedPassword);
                user = users.save(user);
                student = new Student(user.getId(), schoolId);
            } else {
                student = students.findByUserIdAndSchoolId(user.getId(),
                        schoolId);
                if (student == null) {
                    throw new SentinelServerInternalError(
                            "Inconsitent state - no student record for userId: " +
                            user.getId());
                }
            }
            ClubAssociation assoc = new ClubAssociation(club.getId(), user.getId());
            assoc.addRole(ClubAssociation.CLUB_ADMIN);
            assoc = clubAssociations.save(assoc);
            //student.addClubAssociation(assoc.getId());
            //students.save(student);
            System.out.println("Update association for student club-admin " +
                    user.getEmailId());
        }
        return club;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteClub(@PathVariable String id) {
        User sessionUser = userService.getLoggedInUser();

        System.out.println("Attemp to delete club: " + id);
        Club club = clubs.findById(id).get();
        // check if club exists
        if (club == null) {
            throw new RecordNotFoundException("Club " + id + " does not exist");
        }
        // check permissions.
        // Only a SCHOOL-ADMIN or CLUB-SUPERVISOR can delete the club
        if (!sessionUser.getRoles().contains(new Role(Role.TEACHER)) &&
                (!sessionUser.getRoles().contains(new Role(Role.SCHOOL_ADMIN)))) {
            throw new InsufficentAccessException(
                    "Insufficent access - need to have club-supervisor/admin access");
        }
        ClubAssociation assoc = clubAssociations.findByClubIdAndUserId(
                club.getId(), sessionUser.getId());
        if ((assoc == null) ||
            (!assoc.getClubRoles().contains(ClubAssociation.CLUB_SUPERVISOR))) {
            throw new InsufficentAccessException(
                    "Insufficent access - teacher is not club-supervisor");
        }

        // get all associations for this club. 
        HashSet<ClubAssociation> associations = clubAssociations.findAllByClubId(club.getId());
        for (ClubAssociation tAssoc : associations) {
            System.out.println("Deleting association with club member "
                    + tAssoc.getUserId());
            clubAssociations.delete(tAssoc);
        }
        clubs.delete(club);
    }

    // register a user with club
    @RequestMapping(value = "/{id}/register", method = RequestMethod.POST)
    public void registerWithClub(@PathVariable("id") String id,
            @Valid @RequestBody ClubRegister info) {
        User sessionUser = userService.getLoggedInUser();

        Club club = clubs.findById(id).get();
        if (club == null) {
            throw new RecordNotFoundException("Club " + id + " does not exist");
        }
        // club belongs to the same school
        if (!club.getSchoolId().equalsIgnoreCase(sessionUser.getSchoolId())) {
            throw new InsufficentAccessException(
                    "Insufficent access - club does not belong to user's school");
        }

        ClubAssociation assoc = clubAssociations.findByClubIdAndUserId(
                    club.getId(), sessionUser.getId());
        if (assoc != null) {
            throw new RecordAlreadyExistsException("Association with club " + club.getId() +
                    " already exists for user " + sessionUser.getId());
        }

        assoc = new ClubAssociation(club.getId(), sessionUser.getId());

        if (sessionUser.getRoles().contains(new Role(Role.TEACHER))) {
            assoc.addRole(ClubAssociation.CLUB_ADVISOR);
        } else if (sessionUser.getRoles().contains(new Role(Role.STUDENT))) {
            if (info.getIsMember()) {
                assoc.setStatus(ClubAssociation.StatusPending);
                // mark the registration as pending -  needs a club admin to approve it
                assoc.addRole(ClubAssociation.CLUB_MEMBER);
            } else  {
                assoc.addRole(ClubAssociation.CLUB_SUBSCRIBER);
            }
        }
        assoc = clubAssociations.save(assoc);
    }

    // unregister a user with club
    @RequestMapping(value = "/{id}/unregister", method = RequestMethod.POST)
    public void unregisterFromClub(@PathVariable("id") String id,
            @Valid @RequestBody ClubRegister info) {

        User sessionUser = userService.getLoggedInUser();

        Club club = clubs.findById(id).get();
        if (club == null) {
            throw new RecordNotFoundException("Club " + id + " does not exist");
        }
        // club belongs to the same school
        if (!club.getSchoolId().equalsIgnoreCase(sessionUser.getSchoolId())) {
            throw new InsufficentAccessException(
                    "Insufficent access - club does not belong to user's school");
        }
        // check permissions - only student or school-admin can unregister a student from club
        if ((!sessionUser.getId().equalsIgnoreCase(id)) &&
                (!sessionUser.getRoles().contains(new Role(Role.SCHOOL_ADMIN)))) {
            throw new InsufficentAccessException("Insufficent access -  cannot access student info");
        }

        ClubAssociation assoc = clubAssociations.findByClubIdAndUserId(
                club.getId(), sessionUser.getId());
        if (assoc == null) {
            throw new RecordNotFoundException(
                    "No assocition for student " + id +
                    " with Club " + club.getId());
        }

        // delete association from collections
        clubAssociations.delete(assoc);
    }


    // modify club association - change the status of the assocation
    // this action can only be done by a club-admin
    @RequestMapping(value = "/{id}/association", method = RequestMethod.PUT)
    public void modifyAssociationState(@PathVariable("id") String id,
            @Valid @RequestBody Map<String, String> info) {

        User sessionUser = userService.getLoggedInUser();

        Club club = clubs.findById(id).get();
        if (club == null) {
            throw new RecordNotFoundException("Club " + id + " does not exist");
        }
        // club belongs to the same school
        if (!club.getSchoolId().equalsIgnoreCase(sessionUser.getSchoolId())) {
            throw new InsufficentAccessException(
                    "Insufficent access - club does not belong to user's school");
        }

        // check permissions - school-admin can change the association of member
        if (!sessionUser.getRoles().contains(new Role(Role.SCHOOL_ADMIN))) {
            throw new InsufficentAccessException("Insufficent access -  cannot access student info");
        }

        ClubAssociation assoc = clubAssociations.findByClubIdAndUserId(
                club.getId(), sessionUser.getId());
        if (assoc == null) {
            throw new RecordNotFoundException(
                    "No assocition for student " + id +
                    " with Club " + club.getId());
        }

        // get intended change status
        String toStatus = info.get("status");
        if ((toStatus == null) || ((!toStatus.equalsIgnoreCase(ClubAssociation.StatusPending)) &&
                (!toStatus.equalsIgnoreCase(ClubAssociation.StatusApproved)) &&
                (!toStatus.equalsIgnoreCase(ClubAssociation.StatusSuspended)))) {
            throw new InvalidFieldModificationException("Invalid club association status");
        }

        assoc.setStatus(toStatus);
        assoc = clubAssociations.save(assoc);
        System.out.println("Changed status of association " + assoc.getId() + " to " + toStatus);
    }

    // List all associations in a given status
    // this action can only be done by a club-admin
    @RequestMapping(value = "/{id}/association", method = RequestMethod.GET)
    public HashSet<ClubMember> listMembersInStatus(@PathVariable("id") String id,
         @Valid @RequestBody Map<String, String> info) {

        User sessionUser = userService.getLoggedInUser();

        Club club = clubs.findById(id).get();
        if (club == null) {
            throw new RecordNotFoundException("Club " + id + " does not exist");
        }
        // club belongs to the same school
        if (!club.getSchoolId().equalsIgnoreCase(sessionUser.getSchoolId())) {
            throw new InsufficentAccessException(
                    "Insufficent access - club does not belong to user's school");
        }

        // check permissions - school-admin can change the association of member
        if (!sessionUser.getRoles().contains(new Role(Role.SCHOOL_ADMIN))) {
            throw new InsufficentAccessException("Insufficent access -  cannot access student info");
        }

        // get intended change status
        String status = info.get("status");
        if ((status == null) || ((!status.equalsIgnoreCase(ClubAssociation.StatusPending)) &&
                    (!status.equalsIgnoreCase(ClubAssociation.StatusApproved)) &&
                    (!status.equalsIgnoreCase(ClubAssociation.StatusSuspended)))) {
            throw new InvalidFieldModificationException("Invalid club association status");
        }

        // now get the list of members in the intended state
        HashSet<ClubAssociation> assocList =
            clubAssociations.findAllByClubIdAndStatus(club.getId(), status);
        HashSet<ClubMember> memberList = new HashSet<ClubMember>();
        if (assocList == null) {
            System.out.println("No members in status " + status + " for club " +
                                            club.getId());
            return memberList;
        }
        for (ClubAssociation tassoc : assocList) {
            System.out.println(tassoc);
            User user = users.findById(tassoc.getUserId()).get();
            if (user == null) {
                System.out.println("Inconsistent status in db");
                continue;
            }

            ClubMember member = new ClubMember(user, tassoc.getClubRoles());
            member.setStatus(tassoc.getStatus());
            memberList.add(member);
        }
        return memberList;
    }
}
